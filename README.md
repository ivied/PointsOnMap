PinkDot
===========
1. Не отображается карта.

12-23 12:59:17.633: E/Google Maps Android API(7243): Authorization failure.  Please see https://developers.google.com/maps/documentation/android/start for how to correctly set up the map.
12-23 12:59:17.643: E/Google Maps Android API(7243): Ensure that the following correspond to what is in the API Console: Package Name: anywayanyday.pointsonmap, API Key: AIzaSyBWbYYQNreo-rbJlbfULEv03nEEs0Zizbk, Certificate Fingerprint: 6AEEED56FF217EB1BBF8DAE73DFB0B24FA7F70D5

2. Креш при попытке добавить точку без адреса.

12-23 13:01:43.349: E/AndroidRuntime(7849): FATAL EXCEPTION: main
12-23 13:01:43.349: E/AndroidRuntime(7849): java.lang.IndexOutOfBoundsException: Invalid index 0, size is 0
12-23 13:01:43.349: E/AndroidRuntime(7849): 	at java.util.ArrayList.throwIndexOutOfBoundsException(ArrayList.java:251)
12-23 13:01:43.349: E/AndroidRuntime(7849): 	at java.util.ArrayList.get(ArrayList.java:304)
12-23 13:01:43.349: E/AndroidRuntime(7849): 	at anywayanyday.pointsonmap.WorkWithAPI.AsyncGoogleJob.getGeoData(AsyncGoogleJob.java:64)
12-23 13:01:43.349: E/AndroidRuntime(7849): 	at anywayanyday.pointsonmap.WorkWithAPI.AsyncGoogleJob.dataDownload(AsyncGoogleJob.java:44)
12-23 13:01:43.349: E/AndroidRuntime(7849): 	at anywayanyday.pointsonmap.UI.FragmentAddDots.onClick(FragmentAddDots.java:78)
12-23 13:01:43.349: E/AndroidRuntime(7849): 	at android.view.View.performClick(View.java:4255)
12-23 13:01:43.349: E/AndroidRuntime(7849): 	at android.view.View$PerformClick.run(View.java:17459)
12-23 13:01:43.349: E/AndroidRuntime(7849): 	at android.os.Handler.handleCallback(Handler.java:725)
12-23 13:01:43.349: E/AndroidRuntime(7849): 	at android.os.Handler.dispatchMessage(Handler.java:92)
12-23 13:01:43.349: E/AndroidRuntime(7849): 	at android.os.Looper.loop(Looper.java:137)
12-23 13:01:43.349: E/AndroidRuntime(7849): 	at android.app.ActivityThread.main(ActivityThread.java:5159)
12-23 13:01:43.349: E/AndroidRuntime(7849): 	at java.lang.reflect.Method.invokeNative(Native Method)
12-23 13:01:43.349: E/AndroidRuntime(7849): 	at java.lang.reflect.Method.invoke(Method.java:511)
12-23 13:01:43.349: E/AndroidRuntime(7849): 	at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:810)
12-23 13:01:43.349: E/AndroidRuntime(7849): 	at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:577)
12-23 13:01:43.349: E/AndroidRuntime(7849): 	at dalvik.system.NativeStart.main(Native Method)
12-23 13:01:43.399: W/ActivityManager(647):   Force finishing activity anywayanyday.pointsonmap/.MainActivity

3. Креш после добавления точки (имя - 1, адрес - 1), используя Yandex API.
После этого креш происходит при каждом запуске приложения, пока не сделать clear data.

12-23 13:16:53.100: E/AndroidRuntime(4069): FATAL EXCEPTION: AsyncTask #4
12-23 13:16:53.100: E/AndroidRuntime(4069): java.lang.RuntimeException: An error occured while executing doInBackground()
12-23 13:16:53.100: E/AndroidRuntime(4069): 	at android.os.AsyncTask$3.done(AsyncTask.java:299)
12-23 13:16:53.100: E/AndroidRuntime(4069): 	at java.util.concurrent.FutureTask$Sync.innerSetException(FutureTask.java:273)
12-23 13:16:53.100: E/AndroidRuntime(4069): 	at java.util.concurrent.FutureTask.setException(FutureTask.java:124)
12-23 13:16:53.100: E/AndroidRuntime(4069): 	at java.util.concurrent.FutureTask$Sync.innerRun(FutureTask.java:307)
12-23 13:16:53.100: E/AndroidRuntime(4069): 	at java.util.concurrent.FutureTask.run(FutureTask.java:137)
12-23 13:16:53.100: E/AndroidRuntime(4069): 	at android.os.AsyncTask$SerialExecutor$1.run(AsyncTask.java:230)
12-23 13:16:53.100: E/AndroidRuntime(4069): 	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1076)
12-23 13:16:53.100: E/AndroidRuntime(4069): 	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:569)
12-23 13:16:53.100: E/AndroidRuntime(4069): 	at java.lang.Thread.run(Thread.java:856)
12-23 13:16:53.100: E/AndroidRuntime(4069): Caused by: java.lang.NullPointerException
12-23 13:16:53.100: E/AndroidRuntime(4069): 	at anywayanyday.pointsonmap.Core.Dot.getYaDotPostfix(Dot.java:30)
12-23 13:16:53.100: E/AndroidRuntime(4069): 	at anywayanyday.pointsonmap.WorkWithAPI.AsyncYaJob$DownloadImage.doInBackground(AsyncYaJob.java:101)
12-23 13:16:53.100: E/AndroidRuntime(4069): 	at anywayanyday.pointsonmap.WorkWithAPI.AsyncYaJob$DownloadImage.doInBackground(AsyncYaJob.java:1)
12-23 13:16:53.100: E/AndroidRuntime(4069): 	at android.os.AsyncTask$2.call(AsyncTask.java:287)
12-23 13:16:53.100: E/AndroidRuntime(4069): 	at java.util.concurrent.FutureTask$Sync.innerRun(FutureTask.java:305)
12-23 13:16:53.100: E/AndroidRuntime(4069): 	... 5 more
12-23 13:16:53.100: W/ActivityManager(562):   Force finishing activity anywayanyday.pointsonmap/.MainActivity

4. Яндекс-карта не занимает всю площадь экрана по ширине, в отличие от Google-карты.
5. Переключатель с метками Ya/Gg очень не очевиден для пользователя.
6. При переключении YandexAPI/GoogleAPI происходит мерцание экрана (происходит пересоздание activity, наверняка этого можно избежать).
7. На экране просмотра точки отображается кнопка "Назад". Для навигации следует пользоваться средствами action bar'а и/или хардварной/софтварной кнопкой Back.

Замечания по коду:

1. AsyncGoogleJob на самом деле работает синхронно (Geocoder.getFromLocationName вызывается из ui потока), что приводит к подвисанию ui при добавлении точки.
2. Добавление/удаление точки в/из БД по прежнему осуществляется в ui потоке.

Замечания по стилю:
1. Нет единого стиля в написании кода. В eclipse есть Formatter (ctrl+shift+f), форматирующий код в соответствии с заданными настройками.
2. Наличие ворнингов. (Рекомендуется настроить Save actions в eclipse).
3. Все классы находятся в одном package. Рекомендуется придерживаться какой-либо структуры (например, по архитектурным слоям: ui, database, network).