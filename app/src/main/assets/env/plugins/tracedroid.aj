import org.ligi.tracedroid.TraceDroid;
import org.ligi.tracedroid.sending.TraceDroidEmailSender;

TraceDroid.init(ctx);
TraceDroidEmailSender.sendStackTraces("ligi@ligi.de",ctx);
