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

	public static <RETURN> RETURN doIntxn(Function<EntityManager, RETURN> action) throws Exception {
		EntityManager em = getEntityMnager();
		EntityTransaction txn = em.getTransaction();
		txn.begin();
		try {
			RETURN result = action.apply(em);
			txn.commit();
			return result;
		} finally {
			if(txn.isActive()) {
				txn.rollback();
			}
		}
	}

	static EntityManagerFactory newEntityManagerFactory() {
		return Persistence.createEntityManagerFactory("default");
	}

	private static PersistenceUnitInfo archiverPersistenceUnitInfo() {
		return new PersistenceUnitInfo() {
			@Override
			public String getPersistenceUnitName() {
				return "ApplicationPersistenceUnit";
			}

			@Override
			public String getPersistenceProviderClassName() {
				return "org.hibernate.jpa.HibernatePersistenceProvider";
			}

			@Override
			public PersistenceUnitTransactionType getTransactionType() {
				return PersistenceUnitTransactionType.RESOURCE_LOCAL;
			}

			@Override
			public DataSource getJtaDataSource() {
				return null;
			}

			@Override
			public DataSource getNonJtaDataSource() {
				return null;
			}

			@Override
			public List<String> getMappingFileNames() {
				return Collections.emptyList();
			}

			@Override
			public List<java.net.URL> getJarFileUrls() {
				try {
					return Collections.list(this.getClass()
							.getClassLoader()
							.getResources(""));
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			}

			@Override
			public java.net.URL getPersistenceUnitRootUrl() {
				return null;
			}

			@Override
			public List<String> getManagedClassNames() {
				return Collections.emptyList();
			}

			@Override
			public boolean excludeUnlistedClasses() {
				return false;
			}

			@Override
			public SharedCacheMode getSharedCacheMode() {
				return null;
			}

			@Override
			public ValidationMode getValidationMode() {
				return null;
			}

			@Override
			public Properties getProperties() {
				return new Properties();
			}

			@Override
			public String getPersistenceXMLSchemaVersion() {
				return null;
			}

			@Override
			public ClassLoader getClassLoader() {
				return null;
			}

			@Override
			public void addTransformer(ClassTransformer transformer) {

			}

			@Override
			public ClassLoader getNewTempClassLoader() {
				return null;
			}
		};
	}

	public static void main(String[] args) {
		EntityManagerFactory entityManagerFactory = newEntityManagerFactory();
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		Connector cn = new Connector();
		entityManager.persist(cn);
		transaction.commit();
		System.out.println(cn.getId());
		entityManager.close();
	}
}
