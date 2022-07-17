package com.example.criminalintent.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeRepository {
    private static CrimeRepository sCrimeRepository;

    private List<Crime> mCrimes;

    public static CrimeRepository getInstance() {
        if (sCrimeRepository == null)
            sCrimeRepository = new CrimeRepository();
        return sCrimeRepository;
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public void setCrimes(List<Crime> crimes) {
        mCrimes = crimes;
    }

    public Crime getCrime(UUID uuid) {
        for (Crime crime : mCrimes)
            if (crime.getId().equals(uuid))
                return crime;

        return null;
    }

    public int getPosition(UUID uuid) {
        return mCrimes.indexOf(getCrime(uuid));
    }

    public void insertCrime(Crime crime) {
        mCrimes.add(crime);
    }

    public void updateCrime(Crime crime) throws Exception {
        Crime c = getCrime(crime.getId());
        if (c == null)
            throw new Exception("This crime does not exist!!!");

        c.setTitle(crime.getTitle());
        c.setDate(crime.getDate());
        c.setSolved(crime.isSolved());
    }

    public void deleteCrime(Crime crime) throws Exception {
        Crime c = getCrime(crime.getId());
        if (c == null)
            throw new Exception("This crime does not exist!!!");

        mCrimes.remove(c);
    }

    private CrimeRepository() {
        mCrimes = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0);

            mCrimes.add(crime);
        }
    }
}
