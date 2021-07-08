# gloria-mailer

Send bulk emails using excel mail merge.

利用 Excel 合併的方式寄大量的電子郵件。

## 關於本程式

這是一個只有經幾小時開發的小程式，目的是為了幫朋友 Gloria 寄一些郵件。
網路上應該可以找到很多很好的解決方案，只是因為個人很習慣直接自己開發一個。

## 環境需求及運作原理

首先，不能不提的是：這程式需要透過一台 Mail Relay 去寄郵件，如果沒有 Mail Relay
的話，就請直接放棄。

需要準備的資料是一個 Excel 格式的郵遞及套資料的檔，欄位請一定要放在第一列。
另外，當然是郵件的樣版，.eml 格式的，製作方式是先把想要寄的郵件內容（含 Subject) 寄給
自己一份，並下載成 .eml 格式的檔。要填的欄位部份請使用 << >> 括起來，注意，請用半型。
如 Excel 上的欄位叫 email，在郵件樣版上寫 << email >>。

## 安裝與使用

程式需要 Java 16 或以上的版本來執行，請先確定版本，或去下載 JDK

由 github 拉回後，執行 （以下是在 Linux 的執行方法）
```sh
./mvnw package
```
會把程式封裝成 jar 檔，位於 target 目錄下。

執行前，還要準備好一個 yaml 格式的設定檔，檔名如 application-prod.yml

```yaml
application:
  # Email 樣版的路徑
  template: /path/to/email/template.eml
  # Excel 檔的路徑
  data-file: /path/to/excel/file.xlsx
  # 在 Email 的欄位中，email 的欄位
  email-field: email
  # 在 Email 的欄位中，email 的欄位
  name-field: name
  # 測試用，當測試打開時，只去 scan 整個流程，不會真的寄任何郵件
  test-only: true
  # Debug flag 打開時，會寄到自己的信箱，不會寄到真正的收件者。而且只寄前幾封。
  debug: true
  # 這個就是 debug flage 開的時候實際寄到的位址
  recipient-when-debug: debugger@email.on.earth
smtp:
  # 寄件者的 Email
  from: someone@somewhere.on.mars
  # 寄件者的名字 (文字)
  from-name: 'Your name'
  # 收件者如果想回信，收回覆的信箱
  reply-to: another@somewhere.on.venus
  # 收回覆者的名字顯示
  reply-to-name: 'Her name'
  # 要不要打開回覆，不打開的時候，回覆會到寄件者
  use-reply-to: true
  # 以下這幾個是 Mail relay 用的資料，以下僅是參考例，請務必填正確
  auth: false
  starttls: false
  host: the.smtp.host.name
  port: 25
```

## 執行

剛剛做好的設定檔叫 application-prod.yml，其中，prod 的部份是可以改的，而且，在執行上會被選用，
執行的方式為 （請注意 spring-profiles.active=**prod** 的部份)

```sh
java -Dspring.profiles.active=prod -jar mailer-0.0.1.jar
```

## 已知問題

程式目前只分析樣版 email，並只寄出 MultiPart 中 HTML 格式的部份。

## 後記

這程式在幫朋友解決問題後，大概就沒什麼用了。因為急著用，程式中的架構，命名等部份有些不洽當，
但我已經懶得改了，就讓它維持原本風貌。