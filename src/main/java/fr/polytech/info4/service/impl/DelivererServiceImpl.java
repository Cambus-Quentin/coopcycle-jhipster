package fr.polytech.info4.service.impl;

import fr.polytech.info4.domain.Deliverer;
import fr.polytech.info4.repository.DelivererRepository;
import fr.polytech.info4.service.DelivererService;
import fr.polytech.info4.service.dto.DelivererDTO;
import fr.polytech.info4.service.mapper.DelivererMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Deliverer}.
 */
@Service
@Transactional
public class DelivererServiceImpl implements DelivererService {

    private final Logger log = LoggerFactory.getLogger(DelivererServiceImpl.class);

    private final DelivererRepository delivererRepository;

    private final DelivererMapper delivererMapper;

    public DelivererServiceImpl(DelivererRepository delivererRepository, DelivererMapper delivererMapper) {
        this.delivererRepository = delivererRepository;
        this.delivererMapper = delivererMapper;
    }

    @Override
    public DelivererDTO save(DelivererDTO delivererDTO) {
        log.debug("Request to save Deliverer : {}", delivererDTO);
        Deliverer deliverer = delivererMapper.toEntity(delivererDTO);
        deliverer = delivererRepository.save(deliverer);
        return delivererMapper.toDto(deliverer);
    }

    @Override
    public Optional<DelivererDTO> partialUpdate(DelivererDTO delivererDTO) {
        log.debug("Request to partially update Deliverer : {}", delivererDTO);

        return delivererRepository
            .findById(delivererDTO.getId())
            .map(
                existingDeliverer -> {
                    delivererMapper.partialUpdate(existingDeliverer, delivererDTO);
                    return existingDeliverer;
                }
            )
            .map(delivererRepository::save)
            .map(delivererMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DelivererDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Deliverers");
        return delivererRepository.findAll(pageable).map(delivererMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DelivererDTO> findOne(Long id) {
        log.debug("Request to get Deliverer : {}", id);
        return delivererRepository.findById(id).map(delivererMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Deliverer : {}", id);
        delivererRepository.deleteById(id);
    }
}
