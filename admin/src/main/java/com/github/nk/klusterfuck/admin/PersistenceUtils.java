package com.github.nk.klusterfuck.admin;

import com.github.nk.klusterfuck.admin.model.Connector;
//import jersey.repackaged.com.google.common.collect.ImmutableMap;
//import org.hibernate.cfg.AvailableSettings;
//import org.hibernate.dialect.DerbyDialect;
//import org.hibernate.jpa.HibernatePersistenceProvider;

import javax.persistence.*;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
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
