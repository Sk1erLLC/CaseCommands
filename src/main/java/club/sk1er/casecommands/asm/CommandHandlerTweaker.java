package club.sk1er.casecommands.asm;

import club.sk1er.casecommands.tweaker.transformer.ITransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

public class CommandHandlerTweaker implements ITransformer {

    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraftforge.client.ClientCommandHandler", "net.minecraft.command.CommandHandler"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode methodNode : classNode.methods) {
            String methodName = mapMethodName(classNode, methodNode);

            if (methodName.equals("executeCommand") || methodName.equals("func_71556_a")) {
                ListIterator<AbstractInsnNode> iterator = methodNode.instructions.iterator();

                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();

                    if (node instanceof InsnNode && node.getOpcode() == Opcodes.AALOAD) {
                        methodNode.instructions.insertBefore(node.getNext(), makeLowercase());
                        break;
                    }
                }

                break;
            }
        }
    }

    private InsnList makeLowercase() {
        InsnList list = new InsnList();
        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/String", "toLowerCase", "()Ljava/lang/String;", false));
        return list;
    }
}
