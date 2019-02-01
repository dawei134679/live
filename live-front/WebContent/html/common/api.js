baseUrl = "http://192.168.20.251:8080/TinyPigWebServer";
var api = new Object();
api.sendCode = baseUrl+"/auth/sendcode";
api.register = baseUrl+"/auth/regist";
api.getOrderByNoNew = baseUrl+"/pay/getOrderByNoNew";
api.imageVerifyCode = baseUrl+"/auth/imageVerifyCode";
api.getWebChatSign = baseUrl+"/maiyaShare/getWebChatSign";