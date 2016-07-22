Code Hidding Plugin
---------------------
#说明
在proguard-maven-plugin的基础上修改而来，仅供学习参考使用，可以在项目构建过的时候把代码混淆，支持打成jar包和war包。
基本支持Proguard的所有功能。

##使用方法
先进行Maven install

然后在需要混淆代码的工程中加入此插件的依赖
```xml
      <plugin>
				<groupId>com.jiujie</groupId>
				<artifactId>code-hidding-plugin</artifactId>
				<version>1.0</version>
				<executions>
					<execution>
						<phase>package</phase>
						<goals>
							<goal>proguard</goal>
						</goals>
					</execution>
				</executions>
				<configuration>
					<obfuscate>true</obfuscate>
					<attach>true</attach>
					<injar>classes</injar>
					<attachArtifactClassifier>pg</attachArtifactClassifier>
					attach 的作用是在 install 与 deploy 时将生成的 pg 文件也安装与部署
					<proguardInclude>${basedir}/proguard.conf</proguardInclude>
					<outjar>${project.build.finalName}-pg</outjar>
					<libs>
						<lib>${java.home}/lib/rt.jar</lib>
						<lib>${java.home}/lib/jsse.jar</lib>
					</libs>
					<addMavenDescriptor>false</addMavenDescriptor>
				</configuration>
			</plugin>
```
在工程根目录下加入工程配置文件proguard.conf
```properties
# ----------------------------------  
#  通过指定数量的优化能执行  
#  -optimizationpasses n  
# ----------------------------------  
#-optimizationpasses 3  
  
# ----------------------------------  
#   混淆时不会产生形形色色的类名   
#   -dontusemixedcaseclassnames  
# ----------------------------------  
#-dontusemixedcaseclassnames
# ----------------------------------  
#      指定不去忽略非公共的库类  
#  -dontskipnonpubliclibraryclasses  
# ----------------------------------  
  
# ----------------------------------  
#       不预校验  
#    -dontpreverify  
# ----------------------------------  
# -dontpreverify  
#忽略所有告警
-ignorewarnings
#不做 shrink
-dontshrink
#不做 optimize
-dontoptimize


# ----------------------------------  
#      输出生成信息  
#       -verbose  
# ----------------------------------  
-verbose  
  
#混淆时应用侵入式重载   
#-overloadaggressively   
   
#优化时允许访问并修改有修饰符的类和类的成员   
#-allowaccessmodification  
#确定统一的混淆类的成员名称来增加混淆   
  
  
#这里添加你不需要混淆的类  
#-keep  class com.showjoy.common.cache.** {*;}   
-keepclasseswithmembers class com.showjoy.cart.service.** {  
    <fields>;  
}  
-keepclasseswithmembers class com.showjoy.cart.ui.cart.** {  
    <fields>;  
}  
-keepclassmembers class com.showjoy.cart.CartControllerHandler {  
    <fields>;  
}  
-keepclassmembers class com.showjoy.cart.CartExceptionHandler {  
    <fields>;  
}  
-keep class com.showjoy.cart.util.EncodeUtil {*;}   

#-keep public class * extends  javax.servlet.Servlet  


#-keepdirectories  **  
#-keepattributes **  
#-useuniqueclassmembernames  
#保持源码名与行号（异常时有明确的栈信息），注解（默认会过滤掉所有注解，会影响框架的注解）
-keepattributes SourceFile,LineNumberTable,*Annotation*
#保持包注解类
-keepattributes Signature

#-keepnames class * implements java.io.Serializable  
# ---------保护所有实体中的字段名称----------  
#-keepclassmembers class * implements java.io.Serializable {  
#    <fields>;  
#}  
  
# --------- 保护类中的所有方法名 ------------  
#-keepclassmembers class * {  
#    public <methods>;  
#}  
```
##ProGuard配置
```comment
http://proguard.sourceforge.net/

ProGuard的使用是为了:

1.创建紧凑的代码文档是为了更快的网络传输,快速装载和更小的内存占用.

2.创建的程序和程序库很难使用反向工程.

3.所以它能删除来自源文件中的没有调用的代码

4.充分利用java6的快速加载的优点来提前检测和返回java6中存在的类文件.

参数：

-include {filename} 从给定的文件中读取配置参数

-basedirectory {directoryname} 指定基础目录为以后相对的档案名称

-injars {class_path} 指定要处理的应用程序jar,war,ear和目录

-outjars {class_path} 指定处理完后要输出的jar,war,ear和目录的名称

-libraryjars {classpath} 指定要处理的应用程序jar,war,ear和目录所需要的程序库文件

-dontskipnonpubliclibraryclasses 指定不去忽略非公共的库类。

-dontskipnonpubliclibraryclassmembers 指定不去忽略包可见的库类的成员。

保留选项

-keep {Modifier} {class_specification} 保护指定的类文件和类的成员

-keepclassmembers {modifier} {class_specification} 保护指定类的成员，如果此类受到保护他们会保护的更好

-keepclasseswithmembers {class_specification} 保护指定的类和类的成员，但条件是所有指定的类和类成员是要存在。

-keepnames {class_specification} 保护指定的类和类的成员的名称（如果他们不会压缩步骤中删除）

-keepclassmembernames {class_specification} 保护指定的类的成员的名称（如果他们不会压缩步骤中删除）

-keepclasseswithmembernames {class_specification} 保护指定的类和类的成员的名称，如果所有指定的类成员出席（在压缩步骤之后）

-printseeds {filename} 列出类和类的成员-keep选项的清单，标准输出到给定的文件

压缩

-dontshrink 不压缩输入的类文件

-printusage {filename}

-whyareyoukeeping {class_specification}

优化

-dontoptimize 不优化输入的类文件

-assumenosideeffects {class_specification} 优化时假设指定的方法，没有任何副作用

-allowaccessmodification 优化时允许访问并修改有修饰符的类和类的成员

混淆

-dontobfuscate 不混淆输入的类文件

-printmapping {filename}

-applymapping {filename} 重用映射增加混淆

-obfuscationdictionary {filename} 使用给定文件中的关键字作为要混淆方法的名称

-overloadaggressively 混淆时应用侵入式重载

-useuniqueclassmembernames 确定统一的混淆类的成员名称来增加混淆

-flattenpackagehierarchy {package_name} 重新包装所有重命名的包并放在给定的单一包中

-repackageclass {package_name} 重新包装所有重命名的类文件中放在给定的单一包中

-dontusemixedcaseclassnames 混淆时不会产生形形色色的类名

-keepattributes {attribute_name,...} 保护给定的可选属性，例如LineNumberTable, LocalVariableTable, SourceFile, Deprecated, Synthetic, Signature, and InnerClasses.

-renamesourcefileattribute {string} 设置源文件中给定的字符串常量

因为我们开发的是webwork+spring+hibernate的架构的项目，所有需要很详细的配置。（经过n次失败后总结）

Example:

-injars <project>.jar

-outjars <project>_out.jar

-libraryjars <java.home>/lib/rt.jar

-libraryjars <project.home>/webroot/WEB-INF/lib/webwork.jar

.......

# 保留实现Action接口类中的公有的，友好的，私有的属性 和 公有的，友好的方法。其它的全部压缩，优化，混淆。

# 因为配置文件中的类名是一个完整的类名，如果经过处理后就有可能找不到这个类。

# 属性是jsp页面所需要的，如果经过处理jsp页面就无法得到action中的数据。

-keep public class * implements com.opensymphony.xwork.Action{

public protected private <fields>;

public protected <methods>;

}

# 保留实现了Serializable接口类中的公有的，友好的，私有的成员（属性和方法）

# 这个配置主要是对应实体类的配置。

-keep public class * implements java.io.Serializable{

public protected private *;

}

......
```

#ProGuard Maven Plugin
Run [ProGuard] as part of your [Maven] build. For usage, please read the
generated [Documentation](http://wvengen.github.io/proguard-maven-plugin/).

Development happens at [Github](https://github.com/wvengen/proguard-maven-plugin).
This plugin is currently not under active development, but pull requests are
welcome.

This is the successor of the [ProGuard Maven Plugin by pyx4me](http://pyx4me.com/pyx4me-maven-plugins/proguard-maven-plugin/).


[![Build Status](https://travis-ci.org/wvengen/proguard-maven-plugin.svg?branch=master)](https://travis-ci.org/wvengen/proguard-maven-plugin)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.wvengen/proguard-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.wvengen/proguard-maven-plugin)


[ProGuard]: http://proguard.sourceforge.net/
[Maven]: http://apache.maven.org/
