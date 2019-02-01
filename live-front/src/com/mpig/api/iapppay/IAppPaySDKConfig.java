package com.mpig.api.iapppay;

/**
 *应用接入iAppPay云支付平台sdk集成信息 
 */
public class IAppPaySDKConfig{

	/**
	 * 应用名称：
	 * 应用在iAppPay云支付平台注册的名称
	 */
	public final static  String APP_NAME = "糖谷直播";

	/**
	 * 应用编号：
	 * 应用在iAppPay云支付平台的编号，此编号用于应用与iAppPay云支付平台的sdk集成 
	 */
	public final static  String APP_ID = "3012424275";

	/**
	 * 商品编号：
	 * 应用的商品在iAppPay云支付平台的编号，此编号用于iAppPay云支付平台的sdk到iAppPay云支付平台查找商品详细信息（商品名称、商品销售方式、商品价格）
	 * 编号对应商品名称为：糖果
	 */
	public final static  int WARES_ID_1=1;

	/**
	 * 应用私钥：
	 * 用于对商户应用发送到平台的数据进行加密
	 */
	public final static String APPV_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOijnWYB+41zlZgAS8XcQcgCnEO/kCeE2kzEg5qzM1v3T1sRJr/SYaBdH5WvCkPn2JU7Pw1+ENvfpMk2kwEMv5Nzo87gDnnD6CbLKjn4KUPucym3xscXY2Ri/iJ4cU+z9hjv855cfTv94F7ztfNt7UKVB3AqInxzzJPAml7rmbavAgMBAAECgYAuN7MhfOtY4smpdQWYvXVkIwCghQJCl6Y28iOjLE7byno9gV7NZSJ4FYgc2LWVYA4rN6YDLDR1Oi0mvGzgHIRCigl0FkQBt4he946hoKe6w8qYQqXiWZxLznMXlOXuFpL9yV76PAAVmJLYyY7Lf+BSVS+JKVOO6+VpXY3boT+VwQJBAPsJQ4JF+0bJL4ENcGUuAg4LJHiLO+wzTET3Km22EHXDhBb/h6vfZQCkt9XDKgsQ2kSFrAWWi5J7tnPdSAW7iVkCQQDtPTnQSwzuBM48jbP1vKosbrRfR2ZfCy4p2JrsL6qWf5Iqk0fJWFytod5mE7gwNwujISPXOKXIJFMimTPbqLdHAkEAg9oLwULshfNppgje/eW6YpHrp/zJjngrGRYRDOrRnmkA2euA4P9G152siPQnAwTo/5COF6lIybl8zUJub8RekQJANrU8xMzZ0XWoPYyL6Go2cAKjc6lacSZ54cqCh17CnuwSP+Ew17yZuqyuRancernm5Bp7UmFM0aeWyE6+B7STdwJBAIB4usF12amHBY9567ATh6lFuHj+7kT7p0FpwuKluBNMPL+QQSWzIQP3ZjLClnzNhuQfv2+Kh7EBZ5xp2v3bADg=";

	/**
	 * 平台公钥：
	 * 用于商户应用对接收平台的数据进行解密
	
	 */
	public final static String PLATP_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCrN3r+TajdyMsEWV01oDjetJt0N9w3b5ibRUgTMhawsjM+hee80MXzWO5cLtjXen2evoQMOt+rE9EOsP9t1I6G7sgn0eybD9L9yAWTFkT/p0REtr3j3V96Gy5D2tX+Gv+B5eXzchJL8xi0FHnAsJ8m6ByEtDLHe/8WrUl4KJAXdwIDAQAB";
	/**   -----------------------------------------------------小猪WEB-------------------------------------------------------------------- **/
	/**
	 * 应用名称：
	 * 应用在iAppPay云支付平台注册的名称
	 */
	public final static  String WEB_APP_NAME = "小猪WEB";

	/**
	 * 应用编号：
	 * 应用在iAppPay云支付平台的编号，此编号用于应用与iAppPay云支付平台的sdk集成 
	 */
	public final static  String WEB_APP_ID = "3007617619";
	public final static String H5_APP_ID = "3007122344";

	/**
	 * 商品编号：
	 * 应用的商品在iAppPay云支付平台的编号，此编号用于iAppPay云支付平台的sdk到iAppPay云支付平台查找商品详细信息（商品名称、商品销售方式、商品价格）
	 * 编号对应商品名称为：1
	 */
	public final static  int WEB_WARES_ID_1=1;

	/**
	 * 应用私钥：
	 * 用于对商户应用发送到平台的数据进行加密
	 */
	public final static String WEB_APPV_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIZSQ443lxLLSNzr20fHbFY3Issow+F5IQuLaazCWlVGSv6Ae58Uf6JpuyQVRW1i+wxYlgVI3S2CHirfrt9eExZ3TK1N2KMBJg3Rs8E7mdK4be1on6A1sbmpYF0Ul8mMO2BMQxem0ANg+ihJHhnw6B2Ao5C+/gE+RDZSe/MJDY0lAgMBAAECgYBuhzLfzA6cgHqsj8gZAJi9ORGAa0MEPlBa15VVe3gpw3XihoczGAMO+kHsJzTCFjjOp/aK0rJtyCxay0VyuE6rc8iF6un0fdpBAsEoWWq3kHqQTiGUPuxo/9Ew50XBUSw8CKsJhtckqIlGl0bHRga4+AkoR8K4P0rBj+LMbknfAQJBAMjXwVFdS1eCaIumvwDd5YBfAozjsa8ol2oey+noxFjwENtzcsJiV+1bbm/mdh3G4dPnj4viLyc4xfmZpOKDgSkCQQCrNbiC/tHytkwoJyjqUroWXOrknJb8tzMgaKQ6kHJGLh6eFWWJ2eLmIRDP7PJp1JAtsVTvAKEg8TshJtUdNH+dAkAfGvs/xLk0dmgGqO6QDc3LkettMp6ESD8bGmXudjRiqduSgVDWtjBz/GMgRYsplXHIRea/fWpcIrjWrWsSAdfxAkAE8f9LYVrlHNjkTAVFh3Aylyp9wfwmfpAufndH7cYvOsPNoyaUrcN9DDV1HA4AepBTkYy3fpgo4G7pzTpYppyNAkAdGcA3H2hysVfOyafkEaFTHmjqWTTVc9/Ve8UBVG7tRvNtYtEnWBHGj2jXXjZ2Hq36mhxfvROMwfAWzoXY8T1m";
	public final static String H5_APPV_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKl44B6rPdG9bYSYFpDWM47Y6m2xbtR4QhMYJuCM37HIX+BGsCWUTiKPxUihb8Ay3rnDDWSVJuAwBYOmLdcjcf9jAzupt/M46xjF/RaYNnih6llWx+XeJdxivrVTn/DnAXYXurDh2/9gSR9JojbbKCprIAVMiG+mO0ytX2/Q6fV9AgMBAAECgYAxWhcCN2H4S06yV0vy/73h8n9KVGpvC1pKjzT7HWsjLbPl7w/q0aZzYCTWgtS1Rtsfg5LJ4LmCfdmNPf6JcTQH7t80uVewhucZ0cYiPLV+C1VYSG9Bi2leOhwOI0jKM/nLM8hulsOPWIOdSMFdkFrfZMZNle+Zu25HUMy6Cr+wrQJBANdHS4qXvgdKuYXnOqvSD9HLbWI2Wbf6IpgObankqG21LIrU+RHkUbJGzEq5JMDp6ILKRmOsl+pzNN/akqRtn3cCQQDJh3E4igevzOwwg0LIFcSy8NnSsRjBdkwWmzC4mEsvwDglJb/6il5xs6Rs2oaoXl+HmX5OXvdBV2h4uwfPOVerAkAX3h2v9fbplA1RnTtSBW4fknBELUZxMVhYh3D9Hw7VavrPQaGDrE6qd1L7Dd/XS5etuINpVZ9aLP+1ueP8OvMtAkBgtP8O66W+pe6dZQ+x5khKuis84MEJbVQb8QLTQjmqQ2pt4QycFLQ3icYYelDuEMN01aJ3xziNgX1fmhioR1MbAkEAxLzYtw5gC88IGN528qs2oTlN+1LfOCsCtwxGOTEAB8k0l//o+NLeWezQw/nyO0kZjpvAxrvEAc9R8zLshqgamg==";

	/**
	 * 平台公钥：
	 * 用于商户应用对接收平台的数据进行解密
	
	 */
	public final static String WEB_PLATP_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCWnIE5Gqwi6Zz+sE18KjVU3o3k05NSqY8R7NkLp01pu3pYDuob00EFWNXp43LYYUDppEw+NZ038IVnbH7PLBJHjsIELcIJ7pqlj0FmmeozAq135b8EwvkWzjUMpm+S8mKCbDoUt+J9vytMyZAbHRuX45LaBIa3MLyKrIr4OwzqTQIDAQAB";
	
	public final static String H5_PLATP_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCH5nVlM6r7l3I0/G+93NxE63/1E2z2KqxKxsxqLHe7mv5z5pSKS0/fvC66mELFAmNyQT+1u3h3CWFfYieiHGJGuScFvMu/xZcy3wgITAd1yGiw391FOy/bpdtrE35dysK6XFn5wUiyw43kr6SlTMXvjWlpZ7Vl1+dEHnuxgwmfFwIDAQAB";

}