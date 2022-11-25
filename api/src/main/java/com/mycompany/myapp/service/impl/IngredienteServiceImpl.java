package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Ingrediente;
import com.mycompany.myapp.repository.IngredienteRepository;
import com.mycompany.myapp.service.IngredienteService;
import com.mycompany.myapp.service.dto.IngredienteDTO;
import com.mycompany.myapp.service.mapper.IngredienteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ingrediente}.
 */
@Service
@Transactional
public class IngredienteServiceImpl implements IngredienteService {

    private final Logger log = LoggerFactory.getLogger(IngredienteServiceImpl.class);

    private final IngredienteRepository ingredienteRepository;

    private final IngredienteMapper ingredienteMapper;

    public IngredienteServiceImpl(IngredienteRepository ingredienteRepository, IngredienteMapper ingredienteMapper) {
        this.ingredienteRepository = ingredienteRepository;
        this.ingredienteMapper = ingredienteMapper;
    }

    @Override
    public IngredienteDTO save(IngredienteDTO ingredienteDTO) {
        log.debug("Request to save Ingrediente : {}", ingredienteDTO);
        Ingrediente ingrediente = ingredienteMapper.toEntity(ingredienteDTO);
        ingrediente = ingredienteRepository.save(ingrediente);
        return ingredienteMapper.toDto(ingrediente);
    }

    @Override
    public IngredienteDTO update(IngredienteDTO ingredienteDTO) {
        log.debug("Request to update Ingrediente : {}", ingredienteDTO);
        Ingrediente ingrediente = ingredienteMapper.toEntity(ingredienteDTO);
        ingrediente = ingredienteRepository.save(ingrediente);
        return ingredienteMapper.toDto(ingrediente);
    }

    @Override
    public Optional<IngredienteDTO> partialUpdate(IngredienteDTO ingredienteDTO) {
        log.debug("Request to partially update Ingrediente : {}", ingredienteDTO);

        return ingredienteRepository
            .findById(ingredienteDTO.getId())
            .map(existingIngrediente -> {
                ingredienteMapper.partialUpdate(existingIngrediente, ingredienteDTO);

                return existingIngrediente;
            })
            .map(ingredienteRepository::save)
            .map(ingredienteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IngredienteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ingredientes");
        return ingredienteRepository.findAll(pageable).map(ingredienteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IngredienteDTO> findOne(Long id) {
        log.debug("Request to get Ingrediente : {}", id);
        return ingredienteRepository.findById(id).map(ingredienteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ingrediente : {}", id);
        ingredienteRepository.deleteById(id);
    }
}
