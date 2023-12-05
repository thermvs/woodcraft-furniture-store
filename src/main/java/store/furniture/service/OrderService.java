package store.furniture.service;

import store.furniture.dto.OrderDTO;
import store.furniture.exception.OrderDoesNotExistException;
import store.furniture.exception.FurnitureDoesNotExistException;
import store.furniture.exception.UserDoesNotExistException;
import store.furniture.model.OrderEntity;
import store.furniture.model.FurnitureEntity;
import store.furniture.model.UserEntity;
import store.furniture.repository.OrderRepository;
import store.furniture.repository.FurnitureRepository;
import store.furniture.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final FurnitureRepository furnitureRepository;

  public List<OrderDTO> getAllOrders(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<OrderEntity> orderPage = orderRepository.findAll(pageable);

    List<OrderEntity> orders = orderPage.getContent();
    return mapToOrderDtoList(orders);
  }

  public OrderDTO getOrderById(long id) throws OrderDoesNotExistException {
    OrderEntity order = orderRepository.findById(id)
      .orElseThrow(() -> new OrderDoesNotExistException(id));
    return mapToOrderDto(order);

  }

  @Transactional
  public OrderDTO createOrder(OrderDTO orderDTO) throws UserDoesNotExistException, FurnitureDoesNotExistException {
    OrderEntity order = mapToOrderEntity(orderDTO, takeUser(orderDTO.getUserLogin()), takeOrders(orderDTO));
    List<FurnitureEntity> ticketEntities = new ArrayList<>();
    for (Long ticketId : orderDTO.getFurnitureId()) {
      Optional<FurnitureEntity> ticketOptional = furnitureRepository.findById(ticketId);
      if (ticketOptional.isPresent()) {
        ticketEntities.add(ticketOptional.get());
      } else {
        throw new FurnitureDoesNotExistException(ticketId);
      }
    }
    order.setTicketEntities(ticketEntities);
    OrderEntity createdOrder = orderRepository.save(order);
    return mapToOrderDto(createdOrder);
  }

  @Transactional
  public OrderDTO updateOrder(long id, OrderDTO orderDTO) throws OrderDoesNotExistException, UserDoesNotExistException {
    Optional<OrderEntity> order = orderRepository.findById(id);
    if (order.isPresent()) {
      OrderEntity ord = order.get();
      ord.setDate(orderDTO.getDate());
      UserEntity ott = userRepository.findByLogin(orderDTO.getUserLogin()).orElseThrow(() ->
        new UserDoesNotExistException(orderDTO.getUserLogin()));
      ord.setUser(ott);
      OrderEntity newOrder = orderRepository.save(ord);
      return mapToOrderDto(newOrder);
    }
    throw new OrderDoesNotExistException(id);
  }

  @Transactional
  public void deleteOrder(long id) {
    if (orderRepository.existsById(id)) {
      orderRepository.deleteById(id);
    }
  }

  private OrderDTO mapToOrderDto(OrderEntity order) {
    List<Long> ticketIds = order.getTicketEntities().stream()
      .map(FurnitureEntity::getId)
      .collect(Collectors.toList());
    return new OrderDTO(order.getId(), order.getDate(), order.getUser().getLogin(), ticketIds);
  }

  private List<OrderDTO> mapToOrderDtoList(List<OrderEntity> orders) {
    return orders.stream().map(this::mapToOrderDto)
      .collect(Collectors.toList());
  }

  private List<FurnitureEntity> takeOrders(OrderDTO orderDTO){
    List<Long> tIds = orderDTO.getFurnitureId();
    List<FurnitureEntity> ticketEntities = furnitureRepository.findAllById(tIds);
    return ticketEntities;
  }

  private UserEntity takeUser(String login) throws UserDoesNotExistException {
    UserEntity userEntity = userRepository.findByLogin(login).orElseThrow(() ->
      new UserDoesNotExistException(login));
    return userEntity;
  }

  private OrderEntity mapToOrderEntity(OrderDTO orderDto, UserEntity user, List<FurnitureEntity> tIds) {
    OrderEntity order = new OrderEntity();
    order.setDate(orderDto.getDate());
    order.setUser(user);
    order.setTicketEntities(tIds);
    return order;
  }
}
