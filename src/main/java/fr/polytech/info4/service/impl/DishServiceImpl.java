package fr.polytech.info4.service.impl;

import fr.polytech.info4.domain.Dish;
import fr.polytech.info4.repository.DishRepository;
import fr.polytech.info4.service.DishService;
import fr.polytech.info4.service.dto.DishDTO;
import fr.polytech.info4.service.mapper.DishMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Dish}.
 */
@Service
@Transactional
public class DishServiceImpl implements DishService {

    private final Logger log = LoggerFactory.getLogger(DishServiceImpl.class);

    private final DishRepository dishRepository;

    private final DishMapper dishMapper;

    public DishServiceImpl(DishRepository dishRepository, DishMapper dishMapper) {
        this.dishRepository = dishRepository;
        this.dishMapper = dishMapper;
    }

    @Override
    public DishDTO save(DishDTO dishDTO) {
        log.debug("Request to save Dish : {}", dishDTO);
        Dish dish = dishMapper.toEntity(dishDTO);
        dish = dishRepository.save(dish);
        return dishMapper.toDto(dish);
    }

    @Override
    public Optional<DishDTO> partialUpdate(DishDTO dishDTO) {
        log.debug("Request to partially update Dish : {}", dishDTO);

        return dishRepository
            .findById(dishDTO.getId())
            .map(
                existingDish -> {
                    dishMapper.partialUpdate(existingDish, dishDTO);
                    return existingDish;
                }
            )
            .map(dishRepository::save)
            .map(dishMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DishDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Dishes");
        return dishRepository.findAll(pageable).map(dishMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DishDTO> findOne(Long id) {
        log.debug("Request to get Dish : {}", id);
        return dishRepository.findById(id).map(dishMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Dish : {}", id);
        dishRepository.deleteById(id);
    }
}
