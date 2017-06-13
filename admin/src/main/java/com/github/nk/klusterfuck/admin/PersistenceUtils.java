package com.github.nk.klusterfuck.admin;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.function.Function;

/**
 * Created by nk on 13/6/17.
 */
public class PersistenceUtils {

	private static EntityManagerFactory emf;

	public static <RETURN> RETURN doInTxn(Function<EntityManager, RETURN> action) throws Exception {
		EntityManager em = getEntityMnager();
		EntityTransaction txn = em.getTransaction();
		txn.begin();
		try {
			RETURN result = action.apply(em);
			txn.commit();
			return result;
		} catch(Exception e) {
			if(txn.isActive()) {
				txn.rollback();
			}
			throw e;
		}
	}

	private static EntityManager getEntityMnager() {
		synchronized(PersistenceUtils.class) {
			if (emf == null) {
				synchronized(PersistenceUtils.class) {
					emf = newEntityManagerFactory();
				}
			}
		}
		return emf.createEntityManager();
	}

	static EntityManagerFactory newEntityManagerFactory() {
		return Persistence.createEntityManagerFactory("default");
	}

}
