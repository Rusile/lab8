package Rusile.client.models;

import Rusile.client.networkManager.ClientSocketChannelIO;
import Rusile.client.util.LanguagesEnum;
import javafx.stage.Stage;

public class AbstractModel {
    //private final String clientInfo;
    private final ClientSocketChannelIO clientSocketChannelIO;
    private final Stage currentStage;

    public AbstractModel(ClientSocketChannelIO clientSocketChannelIO, Stage currentStage) {
        this.clientSocketChannelIO = clientSocketChannelIO;
        this.currentStage = currentStage;
        //this.clientInfo = clientSocketChannelIO.getInetAddress().getAddress() + ":" + clientSocketChannelIO.getInetAddress().getPort();
    }
    public LanguagesEnum getLanguage(String s) {
        if ("ru".equals(s)) {
            return LanguagesEnum.RUSSIAN;
        } else if ("is".equals(s)) {
            return LanguagesEnum.ICELANDIC;
        } else if ("sv".equals(s)) {
            return LanguagesEnum.SVENSKA;
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

    //public String getClientInfo() {
    //    return clientInfo;
  //  }
}
