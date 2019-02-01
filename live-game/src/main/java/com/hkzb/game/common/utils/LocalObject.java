package com.hkzb.game.common.utils;

import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import org.apache.commons.codec.binary.Base64;

public abstract class LocalObject<T> {

	protected abstract T create();

	private ThreadLocal<T> holder = new ThreadLocal<T>();

	public T get() {
		T o = holder.get();
		if (o == null) {
			o = create();
			holder.set(o);
		}
		return o;
	}

	public final static LocalObject<MessageDigest> md5 = new LocalObject<MessageDigest>() {

		@Override
		protected MessageDigest create() {
			try {
				return MessageDigest.getInstance("md5");
			} catch (NoSuchAlgorithmException e) {
				// impossible
				return null;
			}
		}

	};

	public final static LocalObject<Random> random = new LocalObject<Random>() {

		@Override
		protected Random create() {
			return new Random();
		}
	};

	public final static LocalObject<KeyPairGenerator> rsaKeyGen = new LocalObject<KeyPairGenerator>() {

		@Override
		protected KeyPairGenerator create() {
			try {
				KeyPairGenerator gen = KeyPairGenerator.getInstance("rsa");
				gen.initialize(512);
				return gen;
			} catch (NoSuchAlgorithmException e) {
				return null;
			}
		}

	};

	public final static LocalObject<KeyGenerator> desKeyGen = new LocalObject<KeyGenerator>() {

		@Override
		protected KeyGenerator create() {
			try {
				KeyGenerator gen = KeyGenerator.getInstance("des");
				return gen;
			} catch (NoSuchAlgorithmException e) {
				return null;
			}
		}

	};

	public final static LocalObject<Cipher> rsaCipher = new LocalObject<Cipher>() {

		@Override
		protected Cipher create() {
			try {
				return Cipher.getInstance("rsa");
			} catch (Exception e) {
				return null;
			}
		}
	};
	
	public final static LocalObject<Cipher> desCipher = new LocalObject<Cipher>() {

		@Override
		protected Cipher create() {
			try {
				return Cipher.getInstance("des");
			} catch (Exception e) {
				return null;
			}
		}
	};

	public final static LocalObject<Base64> base64 = new LocalObject<Base64>() {

		@Override
		protected Base64 create() {
			return new Base64();
		}
	};

	public final static LocalObject<SimpleDateFormat> dayFormatter = new LocalObject<SimpleDateFormat>() {

		@Override
		protected SimpleDateFormat create() {
			return new SimpleDateFormat("yyyyMMdd");
		}

	};
}
