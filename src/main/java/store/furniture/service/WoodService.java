package store.furniture.service;

import store.furniture.dto.WoodDTO;
import store.furniture.exception.*;
import store.furniture.model.*;
import store.furniture.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WoodService {
  private final WoodRepository woodRepository;

  public List<WoodDTO> getAllWoods(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<WoodEntity> orderPage = woodRepository.findAll(pageable);

    List<WoodEntity> Woods = orderPage.getContent();
    return mapToWoodDtoList(Woods);
  }

  public WoodDTO getWoodById(long id) throws WoodDoesNotExistException {
    WoodEntity Wood = woodRepository.findById(id)
      .orElseThrow(() -> new WoodDoesNotExistException(id));
    return mapToWoodDto(Wood);

  }

  public WoodDTO createWood(WoodDTO woodDTO) {
    WoodEntity Wood = mapToWoodEntity(woodDTO);
    WoodEntity createdWood = woodRepository.save(Wood);
    return mapToWoodDto(createdWood);
  }

  @Transactional
  public WoodDTO updateWood(long id, WoodDTO woodDTO) throws WoodDoesNotExistException {
    Optional<WoodEntity> Wood = woodRepository.findById(id);
    if (Wood.isPresent()) {
      WoodEntity woodEntity = Wood.get();
      woodEntity.setName(woodDTO.getName());
      woodEntity.setDoneDate(woodDTO.getDoneDate());

      WoodEntity newWood = woodRepository.save(woodEntity);
      return mapToWoodDto(newWood);
    }
    throw new WoodDoesNotExistException(id);
  }

  @Transactional
  public void deleteWood(long id) {
    if (woodRepository.existsById(id)) {
      woodRepository.deleteById(id);
    }
  }

  private WoodDTO mapToWoodDto(WoodEntity Wood) {
    return new WoodDTO(Wood.getId(), Wood.getName(), Wood.getDoneDate());

  }

  private List<WoodDTO> mapToWoodDtoList(List<WoodEntity> Woods) {
    return Woods.stream().map(this::mapToWoodDto)
      .collect(Collectors.toList());
  }

  private WoodEntity mapToWoodEntity(WoodDTO woodDTO) {
    WoodEntity Wood = new WoodEntity();
    Wood.setName(woodDTO.getName());
    Wood.setDoneDate(woodDTO.getDoneDate());
    return Wood;
  }
}
