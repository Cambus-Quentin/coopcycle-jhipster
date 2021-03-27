package fr.polytech.info4.service.impl;

import fr.polytech.info4.domain.LocalCooperative;
import fr.polytech.info4.repository.LocalCooperativeRepository;
import fr.polytech.info4.service.LocalCooperativeService;
import fr.polytech.info4.service.dto.LocalCooperativeDTO;
import fr.polytech.info4.service.mapper.LocalCooperativeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LocalCooperative}.
 */
@Service
@Transactional
public class LocalCooperativeServiceImpl implements LocalCooperativeService {

    private final Logger log = LoggerFactory.getLogger(LocalCooperativeServiceImpl.class);

    private final LocalCooperativeRepository localCooperativeRepository;

    private final LocalCooperativeMapper localCooperativeMapper;

    public LocalCooperativeServiceImpl(
        LocalCooperativeRepository localCooperativeRepository,
        LocalCooperativeMapper localCooperativeMapper
    ) {
        this.localCooperativeRepository = localCooperativeRepository;
        this.localCooperativeMapper = localCooperativeMapper;
    }

    @Override
    public LocalCooperativeDTO save(LocalCooperativeDTO localCooperativeDTO) {
        log.debug("Request to save LocalCooperative : {}", localCooperativeDTO);
        LocalCooperative localCooperative = localCooperativeMapper.toEntity(localCooperativeDTO);
        localCooperative = localCooperativeRepository.save(localCooperative);
        return localCooperativeMapper.toDto(localCooperative);
    }

    @Override
    public Optional<LocalCooperativeDTO> partialUpdate(LocalCooperativeDTO localCooperativeDTO) {
        log.debug("Request to partially update LocalCooperative : {}", localCooperativeDTO);

        return localCooperativeRepository
            .findById(localCooperativeDTO.getId())
            .map(
                existingLocalCooperative -> {
                    localCooperativeMapper.partialUpdate(existingLocalCooperative, localCooperativeDTO);
                    return existingLocalCooperative;
                }
            )
            .map(localCooperativeRepository::save)
            .map(localCooperativeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LocalCooperativeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LocalCooperatives");
        return localCooperativeRepository.findAll(pageable).map(localCooperativeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LocalCooperativeDTO> findOne(Long id) {
        log.debug("Request to get LocalCooperative : {}", id);
        return localCooperativeRepository.findById(id).map(localCooperativeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LocalCooperative : {}", id);
        localCooperativeRepository.deleteById(id);
    }
}
