package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Helado;
import com.mycompany.myapp.repository.HeladoRepository;
import com.mycompany.myapp.service.HeladoService;
import com.mycompany.myapp.service.dto.HeladoDTO;
import com.mycompany.myapp.service.mapper.HeladoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Helado}.
 */
@Service
@Transactional
public class HeladoServiceImpl implements HeladoService {

    private final Logger log = LoggerFactory.getLogger(HeladoServiceImpl.class);

    private final HeladoRepository heladoRepository;

    private final HeladoMapper heladoMapper;

    public HeladoServiceImpl(HeladoRepository heladoRepository, HeladoMapper heladoMapper) {
        this.heladoRepository = heladoRepository;
        this.heladoMapper = heladoMapper;
    }

    @Override
    public HeladoDTO save(HeladoDTO heladoDTO) {
        log.debug("Request to save Helado : {}", heladoDTO);
        Helado helado = heladoMapper.toEntity(heladoDTO);
        helado = heladoRepository.save(helado);
        return heladoMapper.toDto(helado);
    }

    @Override
    public HeladoDTO update(HeladoDTO heladoDTO) {
        log.debug("Request to update Helado : {}", heladoDTO);
        Helado helado = heladoMapper.toEntity(heladoDTO);
        helado = heladoRepository.save(helado);
        return heladoMapper.toDto(helado);
    }

    @Override
    public Optional<HeladoDTO> partialUpdate(HeladoDTO heladoDTO) {
        log.debug("Request to partially update Helado : {}", heladoDTO);

        return heladoRepository
            .findById(heladoDTO.getId())
            .map(existingHelado -> {
                heladoMapper.partialUpdate(existingHelado, heladoDTO);

                return existingHelado;
            })
            .map(heladoRepository::save)
            .map(heladoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HeladoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Helados");
        return heladoRepository.findAll(pageable).map(heladoMapper::toDto);
    }

    public Page<HeladoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return heladoRepository.findAllWithEagerRelationships(pageable).map(heladoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HeladoDTO> findOne(Long id) {
        log.debug("Request to get Helado : {}", id);
        return heladoRepository.findOneWithEagerRelationships(id).map(heladoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Helado : {}", id);
        heladoRepository.deleteById(id);
    }
}
