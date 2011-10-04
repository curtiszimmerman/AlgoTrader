package com.algoTrader.util;

import org.hibernate.LockOptions;
import org.hibernate.SessionFactory;

public class HibernateUtil {

	public static void lock(SessionFactory sessionFactory, Object target) {

		sessionFactory.getCurrentSession().buildLockRequest(LockOptions.NONE).lock(target);
	}
}
