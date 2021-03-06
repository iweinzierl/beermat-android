package de.inselhome.beermat.persistence;


import de.inselhome.beermat.domain.Bill;
import de.inselhome.beermat.exception.BillPersistenceException;

import java.util.List;

public interface BillRepository {

    List<Bill> getAll() throws BillPersistenceException;

    Bill get(long id) throws BillPersistenceException;

    Bill save(Bill bill) throws BillPersistenceException;

    void delete(Bill bill) throws BillPersistenceException;

    Bill saveAsProfile(Bill bill) throws BillPersistenceException;

    List<Bill> getAllProfiles() throws BillPersistenceException;

    void deleteProfile(Bill bill) throws BillPersistenceException;
}
