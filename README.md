# BuildingWordBattle
建築伝言ゲーム

## セットアップに必要なこと
- ワールドはフラット
- 以上！

## 仕様リスト
- [x] Spigot/Paper 1.12.2 + OpenJDK17
- [x] 途中参加は自動で観戦(スペク)へ
- [x] 出題者は180秒 (configで変更可能)
- [ ] 担当外 - 放送主はコマンドでスタート前に設定 (放送主はDiscordの専用チャンネルに出題 (放送見て推測する対策))
- [x] 出題者は右側のスコア、残り時間は上のボスバー(まあこれは任せる)
- [x] 最後の回答者終わったら最初の出題から流れを自動で移動しつつ確認できるようにする

- [x] ゲームをちゃんと遊べるようにする
- [x] ゲーム中のタイマーを作る
- [x] タイマーが0になったときに次のボックスを作る
- [x] デバッグ無し - プレイヤーが抜けた場合、そのボックスを回さずに前のプレイヤーの回答または、建築を見せる

まあGarticPhoneみたいなやつ