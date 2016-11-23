package nsu.fit.shamova;

import nsu.fit.shamova.messages.MessageHolder;
import nsu.fit.shamova.messages.impl.DisconnectMessage;
import nsu.fit.shamova.messages.impl.ParentMessage;
import nsu.fit.shamova.messages.impl.RootMessage;

import java.util.Iterator;

class DisconnectHook implements Runnable {

    private final Node node;

    public DisconnectHook(Node node) {
        this.node = node;
    }

    @Override
    public void run() {
        System.out.println("heeeeeeeeeeeeeere");

        if (!node.isRoot()) {
            for (Host child : node.getChildren()) {
                ParentMessage parentMessage = new ParentMessage(child.getAddress(), child.getPort(), node.getParent().getAddress(), node.getParent().getPort());
                node.getController().getMessageToSend().add(new MessageHolder(parentMessage));
            }
            node.getController().getMessageToSend().add(new MessageHolder(new DisconnectMessage(node.getParent().getAddress(), node.getParent().getPort())));
            try {
                Thread.sleep(5000);
                System.out.println("Good bye!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

        Host root;
        if (!node.getChildren().iterator().hasNext()) {
            return;
        }
        Iterator<Host> children = node.getChildren().iterator();
        root = children.next();
        RootMessage rootMessage = new RootMessage(root.getAddress(), root.getPort());
        node.getController().getMessageToSend().add(new MessageHolder(rootMessage));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (children.hasNext()) {
            Host tmp = children.next();
            ParentMessage parentMessage = new ParentMessage(tmp.getAddress(), tmp.getPort(), root.getAddress(), root.getPort());
            node.getController().getMessageToSend().add(new MessageHolder(parentMessage));
        }
        try {
            Thread.sleep(5000);
            System.out.println("Good bye!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
