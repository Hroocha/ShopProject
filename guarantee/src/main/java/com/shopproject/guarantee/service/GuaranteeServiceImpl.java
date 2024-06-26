package com.shopproject.guarantee.service;

import com.shopproject.guarantee.dto.GuaranteeDto;
import com.shopproject.guarantee.entity.Guarantee;
import com.shopproject.guarantee.repository.GuaranteeRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuaranteeServiceImpl implements GuaranteeService {

    private final GuaranteeRepository guaranteeRepository;

    @Override
    @Transactional
    public GuaranteeDto getGuarantee (UUID purchaseId){
         Guarantee guarantee = guaranteeRepository.getGuaranteeByPurchaseId(purchaseId).orElseThrow(()->
                 new NotFoundException("Гарантия не найдена"));
        return new GuaranteeDto(purchaseId, guarantee.getValidUntil());
    }

    @Override
    @Transactional
    public GuaranteeDto setGuarantee(UUID id, Integer validInMonth){
        LocalDate validUntil = LocalDate.now().plusMonths(validInMonth);
        Guarantee guarantee = new Guarantee(id, validUntil);
        guaranteeRepository.save(guarantee);
        return new GuaranteeDto(id,guarantee.getValidUntil());
    }

    @Override
    @Transactional
    public GuaranteeDto stopGuarantee (UUID purchaseId){
        Guarantee guarantee = guaranteeRepository.getGuaranteeByPurchaseId(purchaseId).orElseThrow(()->
                new NotFoundException("Гарантия не найдена"));
        guarantee.setValidUntil(LocalDate.now());
        guaranteeRepository.save(guarantee);
        return new GuaranteeDto(purchaseId,guarantee.getValidUntil());
    }

}
