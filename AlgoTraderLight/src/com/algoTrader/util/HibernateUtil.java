package com.algoTrader.util;

import org.hibernate.impl.SessionImpl;
import org.hibernate.proxy.HibernateProxy;

public class HibernateUtil {

	public static void reloadEntity(Object entity) {

		((SessionImpl) ((HibernateProxy) entity).getHibernateLazyInitializer().getSession()).refresh(entity);
	}
}
