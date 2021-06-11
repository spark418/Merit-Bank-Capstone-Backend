package com.meritamerica.assignment7.services;

import java.util.ArrayList;
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

	@Override
	public CDOffering getBestCDOffering(double depositAmount) {
		List<CDOffering> cdofferings = new ArrayList<CDOffering>();
				cdofferings=getCDOfferings();
		CDOffering bestCDOffer = cdofferings.get(0);
		for(int i=1;i<cdofferings.size();i++) {
			double futureVal=futureValue(depositAmount,cdofferings.get(i).getInterestRate(),cdofferings.get(i).getTerm());
			double bestFutureVal = futureValue(depositAmount,bestCDOffer.getInterestRate(),bestCDOffer.getTerm());
			if(futureVal>bestFutureVal) {
				bestFutureVal=futureVal;
				bestCDOffer=cdofferings.get(i);
			}
		}
			return bestCDOffer;
	}
	
	//to find the future value
	public double futureValue(double presentValue, double interestRate, int term) {
		double futureVal = recursiveFutureValue(presentValue, term, interestRate);
		return futureVal;
	}
	
	public  double recursiveFutureValue(double amount, int years, double interestRate) {
		double futureVal;
		futureVal= amount*power(amount,years,interestRate);
		return futureVal;
	}
	public  double power(double amount, int years, double interestRate){
		double recurTerm;
		if(years==0) {
			return 1;
		} else {
			recurTerm= (1+interestRate)*power(amount, years-1, interestRate);
			return (recurTerm);
		}
	}
}
