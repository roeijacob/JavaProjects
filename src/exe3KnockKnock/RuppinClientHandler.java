package exe3KnockKnock;

import java.io.*;
import java.net.Socket;

public class RuppinClientHandler implements Runnable {
    private final Socket socket;

    public RuppinClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        RuppinRegistrationProtocol protocol = new RuppinRegistrationProtocol();

        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            send(out, protocol.processInput(null));

            while (true) {
                String input = in.readLine();
                if (input == null) break;

                RuppinRegistrationProtocol.Output res = protocol.processInput(input);

                if (res.action != RuppinRegistrationProtocol.Action.NONE) {
                    res = handleAction(protocol, res.action);
                }

                if (!send(out, res)) break;
            }

        } catch (IOException e) {
            System.err.println("ClientHandler error: " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }
    }

    private RuppinRegistrationProtocol.Output handleAction(RuppinRegistrationProtocol protocol,
                                                          RuppinRegistrationProtocol.Action action) {
        switch (action) {

            case TRY_LOGIN: {
                String u = protocol.getUsername();
                String p = protocol.getPassword();

                RuppinClient probe = new RuppinClient(u, "", 0, "");
                RuppinClient client = null;

                synchronized (RuppinRegistrationServer.clients) {
                    int idx = RuppinRegistrationServer.clients.indexOf(probe);
                    if (idx != -1) client = RuppinRegistrationServer.clients.get(idx);
                }

                boolean ok = (client != null && client.getPassword().equals(p));
                return protocol.onLoginResult(ok);
            }

            case TRY_REGISTER: {
                String u = protocol.getUsername();
                String p = protocol.getPassword();
                int years = protocol.getYears();
                String status = protocol.getAcademic();

                RuppinClient probe = new RuppinClient(u, "", 0, "");
                boolean ok;

                synchronized (RuppinRegistrationServer.clients) {
                    if (RuppinRegistrationServer.clients.contains(probe)) {
                        ok = false;
                    } else {
                        RuppinRegistrationServer.clients.add(new RuppinClient(u, p, years, status));
                        ok = true;
                        RuppinRegistrationServer.backupIfNeeded();
                    }
                }

                return protocol.onRegisterResult(ok);
            }

            case APPLY_UPDATE: {
                String u = protocol.getUsername();
                RuppinClient probe = new RuppinClient(u, "", 0, "");
                RuppinClient client;

                synchronized (RuppinRegistrationServer.clients) {
                    int idx = RuppinRegistrationServer.clients.indexOf(probe);
                    if (idx == -1) {
                        return new RuppinRegistrationProtocol.Output("User not found. Goodbye.", RuppinRegistrationProtocol.Action.CLOSE);
                    }
                    client = RuppinRegistrationServer.clients.get(idx);

                    if (protocol.getNewPassword() != null) client.setPassword(protocol.getNewPassword());
                    if (protocol.getNewYears() != null) client.setYearsAtRuppin(protocol.getNewYears());
                    if (protocol.getNewAcademic() != null) client.setAcademicStatus(protocol.getNewAcademic());
                }

                return protocol.onUpdateApplied();
            }

            case CLOSE:
                return new RuppinRegistrationProtocol.Output("Goodbye.", RuppinRegistrationProtocol.Action.CLOSE);

            default:
                return RuppinRegistrationProtocol.Output.say("Protocol error.");
        }
    }

    private boolean send(PrintWriter out, RuppinRegistrationProtocol.Output res) {
        if (res == null) return false;

        if (res.message != null) out.println(res.message);

        return res.action != RuppinRegistrationProtocol.Action.CLOSE;
    }
}
