package com.qiniu.common;

import com.tinypig.admin.util.Constant;

public final class BannerConfig {
	// bucket 存储空间
	public static final String BANNER = Constant.qn_default_bucket;

	public static final String Cover = Constant.qn_liveCover_bucket;

	// bucket 存储空间 对应的 域名地址
	public static final String DOMAIL = Constant.qn_default_bucket_domain;

	public static final String coverDomail = Constant.qn_liveCover_bucket_domain;

	public static String ACCESS_KEY = Constant.qn_accessKey;

	public static String SECRET_KEY = Constant.qn_secretKey;
}
