package nsu.fit.shamova;

import nsu.fit.shamova.messages.impl.DisconnectMessage;
import nsu.fit.shamova.messages.impl.ParentMessage;
import nsu.fit.shamova.messages.impl.RootMessage;

import java.util.Iterator;

public class DisconnectHook implements Runnable {

    private Node node;

    public DisconnectHook(Node node) {
        this.node = node;
    }

    @Override
    public void run() {
        if (!node.isRoot()) {
            for (Host child : node.getChildren()) {
                DisconnectMessage disconnectMessage = new DisconnectMessage(child.getAddress(), child.getPort());
                ParentMessage parentMessage = new ParentMessage(child.getPort(), child.getAddress(), node.getParent().getAddress(), node.getParent().getPort());
                node.getController().getMessageToSend().add(new MessageHolder(disconnectMessage, 0, System.currentTimeMillis()));
                node.getController().getMessageToSend().add(new MessageHolder(parentMessage, 0, System.currentTimeMillis()));
            }
            return;
        }

        Host root;
        if (!node.getChildren().iterator().hasNext()) {
            return;
        }
        Iterator<Host> children = node.getChildren().iterator();
        root = children.next();
        RootMessage rootMessage = new RootMessage(root.getPort(), root.getAddress());
        node.getController().getMessageToSend().add(new MessageHolder(rootMessage, 0, System.currentTimeMillis()));

        while (children.hasNext()) {
            Host tmp = children.next();
            DisconnectMessage disconnectMessage = new DisconnectMessage(tmp.getAddress(), tmp.getPort());
            ParentMessage parentMessage = new ParentMessage(tmp.getPort(), tmp.getAddress(), root.getAddress(), root.getPort());
            node.getController().getMessageToSend().add(new MessageHolder(disconnectMessage, 0, System.currentTimeMillis()));
            node.getController().getMessageToSend().add(new MessageHolder(parentMessage, 0, System.currentTimeMillis()));
        }
    }

}
