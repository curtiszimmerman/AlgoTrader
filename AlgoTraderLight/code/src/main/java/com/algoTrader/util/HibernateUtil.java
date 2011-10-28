package com.algoTrader.util;

import org.hibernate.LockOptions;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateUtil {

	public static boolean lock(SessionFactory sessionFactory, Object target) {

		Session session = sessionFactory.getCurrentSession();

		try {
			session.buildLockRequest(LockOptions.NONE).lock(target);
			return true;
		} catch (NonUniqueObjectException e) {
			//  different object with the same identifier value was already associated with the session
			return false;
		}
	}

	public static Object merge(SessionFactory sessionFactory, Object target) {

		Session session = sessionFactory.getCurrentSession();

		return session.merge(target);
	}
}
