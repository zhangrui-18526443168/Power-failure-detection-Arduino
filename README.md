# Power-failure-detection-Arduino
* 此demo通过发送http请求，利用定时任务检测是否断电
* 如果服务器三分钟内接收到http请求，则判断为未断电；如果服务器超过三分钟没有接受到http请求，则判断为断电
断电信息和电力恢复信息均可以通过钉钉机器人设置提醒
* 模块 W5500 Arduino开发板
