# NettyProject
## 如何使用它
> Step 1.先在 build.gradle(Project:XXXX) 的 repositories 添加:

	    allprojects {
		repositories {
		    ...
		    maven { url "https://jitpack.io" }
		}
	    }

  
> Step 2. 然后在 build.gradle(Module:app) 的 dependencies 添加:

	dependencies {
		 implementation 'com.github.zhishui1314:NettyProject:5.0'
		}
	  }
	  
> 代码解释

        //true 是否处理粘包拆包 \r\n格式
        NettyManager.connetTCP(true, "172.16.100.100", 8080, new NettyMessageListener() {
            @Override
            public void onMessage(int type, String result) {
                switch (type) {
                    case 0://Netty抛出异常 以及断网
                        break;
                    case 1: //连接成功
                        break;
                    case 2://接收数据处理
                        break;
                    case 3://处理心跳 默认30秒内没读没写
                        break;
                    case 4://断线重连
                        break;
                }
            }
        });
        //发送消息  最后一个参数是否以 \r\n结尾发送 为了处理粘包拆包
        NettyManager.sendMsg("", true);
        //断开tcp
        NettyManager.closeTCP();
 
