package fr.polytech.info4.service.impl;

import fr.polytech.info4.domain.Command;
import fr.polytech.info4.repository.CommandRepository;
import fr.polytech.info4.service.CommandService;
import fr.polytech.info4.service.dto.CommandDTO;
import fr.polytech.info4.service.mapper.CommandMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Command}.
 */
@Service
@Transactional
public class CommandServiceImpl implements CommandService {

    private final Logger log = LoggerFactory.getLogger(CommandServiceImpl.class);

    private final CommandRepository commandRepository;

    private final CommandMapper commandMapper;

    public CommandServiceImpl(CommandRepository commandRepository, CommandMapper commandMapper) {
        this.commandRepository = commandRepository;
        this.commandMapper = commandMapper;
    }

    @Override
    public CommandDTO save(CommandDTO commandDTO) {
        log.debug("Request to save Command : {}", commandDTO);
        Command command = commandMapper.toEntity(commandDTO);
        command = commandRepository.save(command);
        return commandMapper.toDto(command);
    }

    @Override
    public Optional<CommandDTO> partialUpdate(CommandDTO commandDTO) {
        log.debug("Request to partially update Command : {}", commandDTO);

        return commandRepository
            .findById(commandDTO.getId())
            .map(
                existingCommand -> {
                    commandMapper.partialUpdate(existingCommand, commandDTO);
                    return existingCommand;
                }
            )
            .map(commandRepository::save)
            .map(commandMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommandDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Commands");
        return commandRepository.findAll(pageable).map(commandMapper::toDto);
    }

    public Page<CommandDTO> findAllWithEagerRelationships(Pageable pageable) {
        return commandRepository.findAllWithEagerRelationships(pageable).map(commandMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommandDTO> findOne(Long id) {
        log.debug("Request to get Command : {}", id);
        return commandRepository.findOneWithEagerRelationships(id).map(commandMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Command : {}", id);
        commandRepository.deleteById(id);
    }
}
