問い合わせフォームから入力された内容は以下の通りです。

お名前（漢字）：${confirmForm.name!}
お名前（かな）：${confirmForm.kana!}
性別：${confirmForm.sex!}
年齢：${confirmForm.age!}歳
職業：${confirmForm.job!}
郵便番号：〒${confirmForm.zipcode!}
住所：${confirmForm.address!}
電話番号：${confirmForm.tel!}
メールアドレス：${confirmForm.email!}

お問い合わせの種類１：${confirmForm.type1!}
お問い合わせの種類２：${confirmForm.type2!}
お問い合わせの内容：
${confirmForm.inquiry!}
アンケート：
<#list confirmForm.survey as sv>
・${sv!}
</#list>
