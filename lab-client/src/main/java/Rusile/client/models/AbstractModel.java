package Rusile.client.models;

import Rusile.client.networkManager.ClientSocketChannelIO;
import Rusile.client.util.LanguagesEnum;
import javafx.stage.Stage;

public class AbstractModel {
    //private final String clientInfo;
    protected final ClientSocketChannelIO clientSocketChannelIO;
    protected final Stage currentStage;

    public AbstractModel(ClientSocketChannelIO clientSocketChannelIO, Stage currentStage) {
        this.clientSocketChannelIO = clientSocketChannelIO;
        this.currentStage = currentStage;
        //this.clientInfo = clientSocketChannelIO.getInetAddress().getAddress() + ":" + clientSocketChannelIO.getInetAddress().getPort();
    }
    public LanguagesEnum getLanguage(String s) {
        if ("en".equals(s)) {
            return LanguagesEnum.ENGLISH;
        } else if ("lt".equals(s)) {
            return LanguagesEnum.LITHUANIAN;
        } else if ("sk".equals(s)) {
            return LanguagesEnum.SLOVAK;
        } else {
            return LanguagesEnum.SPANISH;
        }
    }

    public ClientSocketChannelIO getClientSocketChannelIO() {
        return clientSocketChannelIO;
    }

    public Stage getCurrentStage() {
        return currentStage;
    }

}
