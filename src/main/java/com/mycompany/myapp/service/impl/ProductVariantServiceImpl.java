package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.ProductVariant;
import com.mycompany.myapp.repository.ProductVariantRepository;
import com.mycompany.myapp.service.ProductVariantService;
import com.mycompany.myapp.service.dto.ProductVariantDTO;
import com.mycompany.myapp.service.mapper.ProductVariantMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.ProductVariant}.
 */
@Service
@Transactional
public class ProductVariantServiceImpl implements ProductVariantService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductVariantServiceImpl.class);

    private final ProductVariantRepository productVariantRepository;

    private final ProductVariantMapper productVariantMapper;

    public ProductVariantServiceImpl(ProductVariantRepository productVariantRepository, ProductVariantMapper productVariantMapper) {
        this.productVariantRepository = productVariantRepository;
        this.productVariantMapper = productVariantMapper;
    }

    @Override
    public ProductVariantDTO save(ProductVariantDTO productVariantDTO) {
        LOG.debug("Request to save ProductVariant : {}", productVariantDTO);
        ProductVariant productVariant = productVariantMapper.toEntity(productVariantDTO);
        productVariant = productVariantRepository.save(productVariant);
        return productVariantMapper.toDto(productVariant);
    }

    @Override
    public ProductVariantDTO update(ProductVariantDTO productVariantDTO) {
        LOG.debug("Request to update ProductVariant : {}", productVariantDTO);
        ProductVariant productVariant = productVariantMapper.toEntity(productVariantDTO);
        productVariant = productVariantRepository.save(productVariant);
        return productVariantMapper.toDto(productVariant);
    }

    @Override
    public Optional<ProductVariantDTO> partialUpdate(ProductVariantDTO productVariantDTO) {
        LOG.debug("Request to partially update ProductVariant : {}", productVariantDTO);

        return productVariantRepository
            .findById(productVariantDTO.getId())
            .map(existingProductVariant -> {
                productVariantMapper.partialUpdate(existingProductVariant, productVariantDTO);

                return existingProductVariant;
            })
            .map(productVariantRepository::save)
            .map(productVariantMapper::toDto);
    }

    public Page<ProductVariantDTO> findAllWithEagerRelationships(Pageable pageable) {
        return productVariantRepository.findAllWithEagerRelationships(pageable).map(productVariantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductVariantDTO> findOne(Long id) {
        LOG.debug("Request to get ProductVariant : {}", id);
        return productVariantRepository.findOneWithEagerRelationships(id).map(productVariantMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ProductVariant : {}", id);
        productVariantRepository.deleteById(id);
    }
}
