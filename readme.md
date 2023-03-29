Libraries to use

http4s
fs2
cats effect
cats
circe
quill + some kind of lightweight db
tapir


Scheme of work 
* Build fs2 processor for input temp, store and periodical output adjustment
* Build tapir endpoint to set temp
* Wire up http4s endpoint to fs2 processor
* Next - storage / kafka input / temperature program / overrides / zones on & off
* DI (readerT or macwire?)