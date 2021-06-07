package com.meritamerica.assignment7.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meritamerica.assignment7.exceptions.InvalidAccountDetailsException;
import com.meritamerica.assignment7.models.CDOffering;
import com.meritamerica.assignment7.repository.CDOfferingRepository;

@Service
public class CDOfferingServiceImpl implements CDOfferingService {
	
	@Autowired
	CDOfferingRepository cdOfferingRepository;

	@Override
	public CDOffering addCDOffering(CDOffering cdoffering) throws InvalidAccountDetailsException{
		if((cdoffering.getTerm()==0)||cdoffering.getInterestRate()<=0||cdoffering.getTerm()<1||cdoffering.getInterestRate()>=1){
			throw new InvalidAccountDetailsException("Invalid details");
		}
		return cdOfferingRepository.save(cdoffering);
	}

	@Override
	public List<CDOffering> getCDOfferings() {
		return cdOfferingRepository.findAll();
	}
}
