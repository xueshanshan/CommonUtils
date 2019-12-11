### 代码结构

#### 命名

常量命名全部大写，单词间用下划线隔开。

```java
 private static final String ASSET_EXTRA_HACK_LIB = "lib/hack.dex";
```

静态变量以前缀`s`开始，成员变量以`m`开始。

```java
private Drawable mMyInvestDrawable;
private static Tencent sTencent;
```

抽象类命名使用 Abs 或 Base 开头。

```java
public class AbsListActivity {
    // ...
}
```


包名统一使用小写，点分隔符之间有且仅有一个自然语义的英语单词。

```java
com.huli.android.sdk.widget
```

一些常用的类的命名如下：

| 类型 | 规范 | 举例 |
| --- | --- | --- |
| Activity 类 | 以 Activity 为后缀 | LoginActivity |
| Fragment 类 | 以 Fragment 为后缀 | NewsTitlelFragment |
| Service 类 | 以 Service 为后缀 | DownloadService |
| Adapter 类 | 以 Adapter 为后缀 | NewsDetailAdapter |
| 工具方法类 | 以 Utils 或 Manager 为后缀 | LogUtils |
| BroadcastReceiver 类 | 以 Receiver 为后缀 | ForceOfflineReceiver |
| 自定义共享基础类 | 以 Base 为前缀 | BaseActivity、BaseFragment |
| 抽象类 | 以 Abs或Base 为前缀 | BaseActivity、BaseFragment |
| 异常类 | 以 Exception 为后缀 | RouterException |

#### 实体类结构

实体类中成员变量必须为私有，包含对应`get``set`方法，必须写 toString 方法

```java
public class Agreement {
    private String title;
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Agreement{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
```

> `toString()`可以使用AS提供的快捷键生成。

禁止在实体类中，同时存在对应属性 xxx 的 isXxx()和 getXxx()方法。

#### 格式化

在一个`switch`块内，每个`case`要么通过`break/return`等来终止，要么注释说明程 序将继续执行到哪一个`case`为止;在一个`switch`块内，都必须包含一个`default `语句并且 放在最后，即使空代码。

```java
@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share:
                share(mUrl);
                break;
            default:
                break;
        }
    }
```

大括号的使用约定。如果是大括号内为空，则简洁地写成{}即可，不需要换行;如果是非空代码块则:
- 左大括号前不换行。
- 左大括号后换行。
- 右大括号前换行。
- 右大括号后还有else等代码则不换行;表示终止的右大括号后必须换行。

任何二目、三目运算符的左右两边都需要加一个空格。

> 运算符包括赋值运算符=、逻辑运算符&&、加减乘除符号等。

方法参数在定义和传入时，多个参数逗号后边必须加空格。

左小括号和字符之间不出现空格;同样，右小括号和字符之间也不出现空格;

```
// 反例
if (空格a == b空格)
```

在 if/else/for/while/do 语句中必须使用大括号。即使只有一行代码，避免采用单行的编码方式:`if (condition) statements;`

```java
if (condition) {
    statements;
}
```

注释的双斜线与注释内容之间有且仅有一个空格。

```java
// 这是示例注释，请注意在双斜线之后有一个空格
String ygb = new String();
```

if/for/while/switch/do 等保留字与括号之间都必须加空格。


#### 类成员顺序


按照从上到下的顺序为：常量、静态变量、成员变量、静态代码块、代码块、构造方法、公有方法/保护方法、私有方法、`getter/setter`方法。

当一个类有多个构造方法或者多个同名方法时，这些方法应该按顺序放置在一起。


```java
private class Demo {
	// 常量
	private static final int CONSTANT = 1;
	// 静态变量
	private static int sStaticVariable = 2;
	// 常量
	private int variable;

	static {
		// 静态代码块
	}

	{
		// 代码块
	}

	public void Demo() {
		// 构造方法
	}

	public void add() {
		// public 成员方法
		variable++;
	}

	private void reset() {
		// private 成员方法
		variable = 0;
	}

	public void setVariable(int variable) {
		// setter 方法
		this.variable = variable;
	}

	public int getVariable() {
		// getter 方法
		return variable;
	}
}
```

### 代码技巧

在 long 或者 Long 赋值时，数值后使用大写的 L，不能是小写的 l，小写容易跟数字 1 混淆，造成误解。

```java
long time = 10000L;
```

所有的覆写方法，必须加@Override 注解。

```java
@Override
protected void onSaveInstanceState(Bundle outState) {
   // ...
}

```

Object 的 equals 方法容易抛空指针异常，应使用常量或确定有值的对象来调用 equals。

```java
if (Constants.HX_JS_RECHARGE.equals(cmd)) {
    // ...
}
```


表达异常的分支时，少用 if-else 方式，这种方式可以改写成:

```java
if (condition) {
    return obj; 
}
```

不同逻辑、不同语义、不同业务的代码之间插入一个空行分隔开来以提升可读性。


### Android资源文件命名

资源文件需要带模块前缀`module_`,如果是主工程可省略。如果是sdk中，需要以`sdk_moudle_`开头。

```java

// 主工程
activity_abs_list.xml

// 同一工程下的module

widget_activity_abs_list.xml

// sdk中的module
sdk_widget_activity_abs_list
```

#### layout文件命名

| 类型 | 规范 |
| --- | --- |
| Activity |  以 module_activity 开头 |
| Fragment |  以 module_fragment 开头 |
| Dialog |  以 module_dialog 开头 |
| include |  以 module_include 开头 |
| ListView |  以 module_list_item 开头 |
| RecyclerView |  以 module_recycle_item 开头 |
| GridView |  以 module_grid_item 开头 |

#### drawable命名 

以对应功能开头

```java
module_selector_

module_shape_
```

#### anim命名 

模块名_逻辑名称_[方向|序号]

```java
module_fade_in

module_fade_out
```

#### color文件命名

color 资源使用#AARRGGBB 格式

具体命名规则待确定 ????

#### Style命名

如果使用的是主题以`Theme为后缀`，如果应用于控件，以`Style结尾`。

样式的集成，采用`parent`的结构，显示声明父样式。

```xml
    <style name="BlackToolbarTheme" parent="BlackToolbarThemeBase" />
```

```xml
   <style name="MenuTextStyle">
        <item name="android:textColor">@android:color/white</item>
        <item name="android:textSize">@dimen/dimen_13_dip</item>
    </style>
```

#### id

`id`以对应控件驼峰的首字母缩写为开始。

| 控件 | 缩写 |
| --- | --- |
| LinearLayout | ll |
| RelativeLayout | rl |
| ConstraintLayout | cl |
| ListView | lv |
| ScollView | sv |
| TextView | tv |
| Button | btn |
| ImageView | iv |
| CheckBox | cb |
| RadioButton | rb |
| EditText | et |

```xml

<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="@dimen/dimen_199_dip"
    android:layout_height="@dimen/dimen_320_dip">
    <ImageView
        android:id="@+id/iv_bg"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:scaleType="fitXY" />
    <ImageView
        android:id="@+id/iv_code"
        android:layout_width="@dimen/dimen_80_dip"
        android:layout_height="@dimen/dimen_80_dip"
        android:layout_centerHorizontal="true"
        android:layout_marginTop="@dimen/dimen_125_dip"
        android:scaleType="fitXY" />
</RelativeLayout>
```

> id不需要添加`module_`前缀

#### attr 

为了防止多个自定义`View`属性的冲突，属性的定义遵循如下方式：

比如自定义`DoubleButtonLayout`这个类,它所有属性添加一个唯一前缀:`module_dbl_`,其中`dbl`表示该类名称首字母

```xml
<declare-styleable name="DoubleButtonLayout">
        <attr name="sdk_widget_dbl_leftContent" format="string" />
        <attr name="sdk_widget_dbl_rightContent" format="string" />
        <attr name="sdk_widget_dbl_leftImg" format="reference" />
        <attr name="sdk_widget_dbl_leftStyle" format="reference" />
        <attr name="sdk_widget_dbl_rightStyle" format="reference" />
        <attr name="sdk_widget_dbl_margin" format="dimension" />
    </declare-styleable>
```

### Android 代码技巧

`Activity`间通过隐式`Intent`的跳转，在发出`Intent`之前必须通过`resolveActivity`检查，避免找不到合适的调用组件，造成`ActivityNotFoundException`的异常。

```java
 if (intent.resolveActivity(context.getPackageManager()) != null) { 
    // 可以跳转
 } else {
   // 异常处理
 }
```


因为`ScrollView`和`ListView`等滑动会有问题。尽量使用`NestedScrollView` + `RecyclerView`。


在 Application 的业务初始化代码加入进程判断，确保只在自己需要的进程 初始化。特别是后台进程减少不必要的业务初始化

```java
 private static boolean needInit(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return false;
        }
        int size = activityManager.getRunningAppProcesses().size();
        for (int i = 0; i < size; i++) {
            ActivityManager.RunningAppProcessInfo runningAppProcessInfo = activityManager.getRunningAppProcesses().get(i);
            if (pid == runningAppProcessInfo.pid) {
                return context.getPackageName().equals(runningAppProcessInfo.processName);
            }
        }
        return false;
    }

```

任何时候不要硬编码文件路径，请使用 Android 文件系统 API 访问。

```java
File externalStorage = Environment.getExternalStorageDirectory();
```

当使用外部存储时，必须检查外部存储的可用性。

```java
public static boolean isExternalStorageAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
}
```

SharedPreference 提交数据时，尽量使用 Editor#apply()，而非 Editor#commit()。一般来讲，仅当需要确定提交结果，并据此有后续操作时，才使 用 Editor#commit()。

```java
sp.edit().putLong(SpHelper.SP_COLUMN_RATING_APP_TIME, preTime).apply();
```

所有的`Android`基本组件都不应在没有严格权限控制的情况下，将`android:exported`设置为`true`。

```xml
  <activity android:name=".ui.about.AboutUsActivity"
            android:theme="@style/WhiteToolbarTheme"
            android:exported="false"
            android:screenOrientation="portrait"/>
```

不能使用 System.out.println 打印 log


### Android SDK 规范

#### 包名

在拆分抽取框架的过程中，会产生多个`module`,对于不同的`moudle`现有统一要求规范

一类是`gradle`插件的依赖，及`gradle`本身提供的编译扩展：

该类框架以`com.huli.android.plugin`作为组名

- `com.huli.android.plugin:gradle-tools:xxx` : `gradle`工具类
- `com.huli.android.plugin:hotfix-patch:xxx` : 热修复相关 


一类是`apk`所需要的依赖，及和`java`相关的依赖

该类框架以`com.huli.android.sdk`作为组名

- `com.huli.android.sdk:common-sdk:xxx`
- `com.huli.android.sdk:debug:xxx`
- `com.huli.android.sdk:update:xxx`


#### 资源混淆

对于依赖库和宿主`app`中包含有同名的资源文件时，宿主`app`的资源文件会覆盖依赖库中的文件。

为了避免这种问题的发生，现唯一的解决方式便是统一要求资源文件的命名。

因为暂时只要`com.huli.android.sdk`类型的会出现资源覆盖的问题，所以仅对`sdk`类型的要求。

基于分包命名的规则，现对资源文件命名有如下规则

所有`resource`下的文件，都已`sdk_xxx_`作为前缀。

例如`com.huli.android.sdk:update:xxx`框架，其资源文件命名的前缀为`sdk_update_`

所有文件包括布局文件，`drawable`文件以及`string.xml`,`color.xml`等内部的变量命名。

> 依赖库中存在一些暴露给外部使用的资源文件，该种类型可以不需要改名，例如`common`中的`dimens`。



#### 版本升级说明

三位标识一个版本 `2.5.3`

- 小的改动(基于某个类内部的改动)或者修复`bug`，仅升级第三位。
- 添加了新的业务和工具类，升级第二位。
- 如果添加了无法兼容的版本，升级第一位。



#### 分包规则

> 该分包规则基于现状考虑，后期会进行择优优化

基于现在分模块开发的思想，在抽取一些工具类到不同`module`中时，或多或少在分包会有不同的见解。

基于以下几点考虑：

- 模块中代码量不大，远远小于一个实际项目的代码量。
- 在分模块时，已经将不同的类按照功能和业务加以区分到不同模块
- 便于更快的找到代码，减少不必要的途径

指定如下规则：

对于要打入`aar`中的代码，即`main`目录下的代码，按照某一功能的类的数量，如果数量大于1，则可以创建新`package`存放，否则直接存放在根目录下即可。

```
.
└── com
    └── huli
        └── android
            └── sdk
                └── common
                    ├── FoxIOUtils.java
                    ├── FoxSpUtils.java
                    ├── FoxTrace.java
                    ├── GeneralInfoHelper.java
                    ├── NetworkUtil.java
                    ├── StorageUtil.java
                    ├── UIUtil.java
                    ├── adpter
                    │   └── BaseRecyclerAdapter.java
                    ├── debug
                    │   ├── BaseDebugListActivity.java
                    │   └── DebugListRecyclerAdapter.java
                    ├── html
                    │   ├── HtmlEntity.java
                    │   ├── HtmlParserUtil.java
                    │   ├── HtmlTagHandler.java
                    │   ├── HtmlTagParser.java
                    │   └── HtmlURLSpan.java
                    ├── permission
                    │   ├── PermissionActivity.java
                    │   ├── PermissionHelper.java
                    │   └── PermissionSparseArray.java
                    └── toast
                        ├── FoxToast.java
                        ├── IToastView.java
                        ├── ToastLoop.java
                        ├── ToastManager.java
                        ├── ToastMessage.java
                        └── ToastView.java


```

对于`debug`目录，不做过多要求，只需要把每一个测试入口的`activity`放入到`activity`包下面。

```
└── debug
    ├── MainActivity.java
    ├── MyApplication.java
    ├── acitivity
    │   ├── GeneralInfoActivity.java
    │   ├── HtmlParserActivity.java
    │   ├── NetworkUtilsActivity.java
    │   ├── PermissionTestActivity.java
    │   ├── SpUtilsActivity.java
    │   ├── StorageActivity.java
    │   ├── ToastActivity.java
    │   ├── TraceActivity.java
    │   └── io
    │       ├── IOActivity.java
    │       ├── IOCallback.java
    │       ├── ReadTask.java
    │       └── WriteTask.java
    ├── bean
    │   └── BookBean.java
    ├── listener
    │   └── TestToastInterfaceImpl.java
    └── widget
        ├── TestRedToastView.java
        └── TestWhiteToastView.java

```

#### SDK技巧

外部正在调用或者二方库依赖的接口，不允许修改方法签名，避免对接口调用方产生 影响。接口过时必须加`@Deprecated`注解，并清晰地说明采用的新接口或者新服务是什么。

```java
   /**
     * please use method isNewLogin() instead
     */
    @Deprecated
    public static boolean isLogin() {
        if (sUser == null) {
            getInstance();
        }
        return sUser.mId > 0L;
    }
```

SDK的清单文件中,声明<appliaction></application>时候,请不要用你自己的application,因为会和宿主app的application合并产生冲突,同理,applica的标签属性中,也不要设置任何属性,因为和宿主清单文件中的application合并,合并一样会产生冲突
