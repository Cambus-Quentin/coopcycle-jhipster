package fr.polytech.info4.service.impl;

import fr.polytech.info4.domain.NationalCooperative;
import fr.polytech.info4.repository.NationalCooperativeRepository;
import fr.polytech.info4.service.NationalCooperativeService;
import fr.polytech.info4.service.dto.NationalCooperativeDTO;
import fr.polytech.info4.service.mapper.NationalCooperativeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link NationalCooperative}.
 */
@Service
@Transactional
public class NationalCooperativeServiceImpl implements NationalCooperativeService {

    private final Logger log = LoggerFactory.getLogger(NationalCooperativeServiceImpl.class);

    private final NationalCooperativeRepository nationalCooperativeRepository;

    private final NationalCooperativeMapper nationalCooperativeMapper;

    public NationalCooperativeServiceImpl(
        NationalCooperativeRepository nationalCooperativeRepository,
        NationalCooperativeMapper nationalCooperativeMapper
    ) {
        this.nationalCooperativeRepository = nationalCooperativeRepository;
        this.nationalCooperativeMapper = nationalCooperativeMapper;
    }

    @Override
    public NationalCooperativeDTO save(NationalCooperativeDTO nationalCooperativeDTO) {
        log.debug("Request to save NationalCooperative : {}", nationalCooperativeDTO);
        NationalCooperative nationalCooperative = nationalCooperativeMapper.toEntity(nationalCooperativeDTO);
        nationalCooperative = nationalCooperativeRepository.save(nationalCooperative);
        return nationalCooperativeMapper.toDto(nationalCooperative);
    }

    @Override
    public Optional<NationalCooperativeDTO> partialUpdate(NationalCooperativeDTO nationalCooperativeDTO) {
        log.debug("Request to partially update NationalCooperative : {}", nationalCooperativeDTO);

        return nationalCooperativeRepository
            .findById(nationalCooperativeDTO.getId())
            .map(
                existingNationalCooperative -> {
                    nationalCooperativeMapper.partialUpdate(existingNationalCooperative, nationalCooperativeDTO);
                    return existingNationalCooperative;
                }
            )
            .map(nationalCooperativeRepository::save)
            .map(nationalCooperativeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NationalCooperativeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all NationalCooperatives");
        return nationalCooperativeRepository.findAll(pageable).map(nationalCooperativeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NationalCooperativeDTO> findOne(Long id) {
        log.debug("Request to get NationalCooperative : {}", id);
        return nationalCooperativeRepository.findById(id).map(nationalCooperativeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete NationalCooperative : {}", id);
        nationalCooperativeRepository.deleteById(id);
    }
}
