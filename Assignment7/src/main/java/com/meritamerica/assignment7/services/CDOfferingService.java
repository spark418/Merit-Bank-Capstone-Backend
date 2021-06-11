package com.meritamerica.assignment7.services;

import java.util.List;

import com.meritamerica.assignment7.exceptions.InvalidAccountDetailsException;
import com.meritamerica.assignment7.models.CDOffering;

public interface CDOfferingService {
	public CDOffering addCDOffering(CDOffering cdoffering) throws InvalidAccountDetailsException;
	public List<CDOffering> getCDOfferings();
	public CDOffering getBestCDOffering(double depositAmount);
}
