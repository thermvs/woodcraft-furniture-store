package store.furniture.service;

import store.furniture.dto.FurnitureDTO;
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
public class FurnitureService {
  private final FurnitureRepository furnitureRepository;
  private final WoodRepository woodRepository;

  public List<FurnitureDTO> getAllFurniture(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<FurnitureEntity> furniturePage = furnitureRepository.findAll(pageable);

    List<FurnitureEntity> furniture = furniturePage.getContent();
    return mapToFurnitureDtoList(furniture);
  }

  public FurnitureDTO getFurnitureById(long id) throws FurnitureDoesNotExistException {
    FurnitureEntity Furniture = furnitureRepository.findById(id)
      .orElseThrow(() -> new FurnitureDoesNotExistException(id));
    return mapToFurnitureDto(Furniture);
  }

  @Transactional
  public FurnitureDTO createFurniture(FurnitureDTO furnitureDTO) throws WoodDoesNotExistException{
    FurnitureEntity furniture = mapToFurnitureEntity(furnitureDTO, takeWood(furnitureDTO.getWoodId()));
    FurnitureEntity createdFurniture = furnitureRepository.save(furniture);
    return mapToFurnitureDto(createdFurniture);
  }

  @Transactional
  public FurnitureDTO updateFurniture(long id, FurnitureDTO furnitureDTO) throws FurnitureDoesNotExistException, WoodDoesNotExistException {
    Optional<FurnitureEntity> furniture = furnitureRepository.findById(id);
    if (furniture.isPresent()) {
      FurnitureEntity furnitureEntity = furniture.get();
      furnitureEntity.setDescription(furnitureDTO.getDescription());
      furnitureEntity.setPrice(furnitureDTO.getPrice());
      FurnitureEntity newFurniture = furnitureRepository.save(furnitureEntity);
      return mapToFurnitureDto(newFurniture);
    }
    throw new FurnitureDoesNotExistException(id);
  }

  @Transactional
  public void deleteFurniture(long id) {
    if (furnitureRepository.existsById(id)) {
      furnitureRepository.deleteById(id);
    }
  }

  private FurnitureDTO mapToFurnitureDto(FurnitureEntity furnitureEntity) {
    return new FurnitureDTO(furnitureEntity.getId(), furnitureEntity.getDescription(),
      furnitureEntity.getPrice(), furnitureEntity.getWood().getId());
  }

  private List<FurnitureDTO> mapToFurnitureDtoList(List<FurnitureEntity> FurnitureEntities) {
    return FurnitureEntities.stream().map(this::mapToFurnitureDto)
      .collect(Collectors.toList());
  }

  private WoodEntity takeWood(Long woodId) throws WoodDoesNotExistException {
    WoodEntity wood = woodRepository.findById(woodId).orElseThrow(() ->
      new WoodDoesNotExistException(woodId));
    return wood;
  }

  private FurnitureEntity mapToFurnitureEntity(FurnitureDTO furnitureDto, WoodEntity wood){
    FurnitureEntity furniture = new FurnitureEntity();
    furniture.setDescription(furnitureDto.getDescription());
    furniture.setPrice(furnitureDto.getPrice());
    furniture.setWood(wood);
    return furniture;
  }
}
