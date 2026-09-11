package jp.co.sample.orderentry.command;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import jp.co.sample.orderentry.dao.ClientDao;
import jp.co.sample.orderentry.dao.EmployeeDao;
import jp.co.sample.orderentry.dao.ModelDao;
import jp.co.sample.orderentry.entity.Client;
import jp.co.sample.orderentry.entity.Employee;
import jp.co.sample.orderentry.entity.Model;

/**
 * マスタ（機種／取引先／従業員）を管理するためのコマンドラインツール。
 *
 * 画面（Webブラウザ）ではなく、コマンドプロンプトから実行して
 * マスタデータの一覧表示・追加・変更・削除を行う。
 *
 * 実行方法:
 *   java -cp "target\classes;lib\ojdbc8.jar" jp.co.sample.orderentry.command.MasterManageCommand
 * （bin\run_master_command.bat から実行する想定）
 */
public class MasterManageCommand {

    private final Scanner scanner = new Scanner(System.in);
    private final ModelDao modelDao = new ModelDao();
    private final ClientDao clientDao = new ClientDao();
    private final EmployeeDao employeeDao = new EmployeeDao();

    public static void main(String[] args) {
        new MasterManageCommand().run();
    }

    private void run() {
        while (true) {
            System.out.println();
            System.out.println("=========================================");
            System.out.println(" 受発注管理システム マスタ管理コマンド");
            System.out.println("=========================================");
            System.out.println(" 1. 機種マスタ管理");
            System.out.println(" 2. 取引先マスタ管理");
            System.out.println(" 3. 従業員マスタ管理");
            System.out.println(" 0. 終了");
            System.out.print("番号を入力してください > ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    manageModel();
                    break;
                case "2":
                    manageClient();
                    break;
                case "3":
                    manageEmployee();
                    break;
                case "0":
                    System.out.println("終了します。");
                    return;
                default:
                    System.out.println("入力値が不正です。数字（0〜3）を入力してください。");
            }
        }
    }

    // =========================================================
    // 機種マスタ
    // =========================================================
    private void manageModel() {
        while (true) {
            System.out.println();
            System.out.println("--- 機種マスタ管理 ---");
            System.out.println(" 1. 一覧表示");
            System.out.println(" 2. 追加");
            System.out.println(" 3. 変更");
            System.out.println(" 4. 削除");
            System.out.println(" 0. 上のメニューへ戻る");
            System.out.print("番号を入力してください > ");

            String input = scanner.nextLine().trim();
            try {
                switch (input) {
                    case "1":
                        printModelList();
                        break;
                    case "2":
                        addModel();
                        break;
                    case "3":
                        updateModel();
                        break;
                    case "4":
                        deleteModel();
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("入力値が不正です。数字（0〜4）を入力してください。");
                }
            } catch (SQLException e) {
                System.out.println("エラーが発生しました： " + e.getMessage());
            }
        }
    }

    private void printModelList() throws SQLException {
        List<Model> list = modelDao.findAll();
        System.out.println();
        System.out.printf("%-10s %-20s %s%n", "機種ID", "機種名", "付属情報");
        for (Model model : list) {
            System.out.printf("%-10s %-20s %s%n", model.getModelId(), model.getModelName(), model.getModelInfo());
        }
        System.out.println("件数： " + list.size());
    }

    private void addModel() throws SQLException {
        System.out.print("機種ID > ");
        String modelId = scanner.nextLine().trim();

        if (modelDao.findById(modelId) != null) {
            System.out.println("エラー： 機種ID [" + modelId + "] は既に存在します。");
            return;
        }

        System.out.print("機種名 > ");
        String modelName = scanner.nextLine().trim();
        System.out.print("付属情報 > ");
        String modelInfo = scanner.nextLine().trim();

        Model model = new Model(modelId, modelName, modelInfo);
        modelDao.insert(model);
        System.out.println("機種 [" + modelId + "] を追加しました。");
    }

    private void updateModel() throws SQLException {
        System.out.print("変更対象の機種ID > ");
        String modelId = scanner.nextLine().trim();

        Model model = modelDao.findById(modelId);
        if (model == null) {
            System.out.println("エラー： 機種ID [" + modelId + "] は存在しません。");
            return;
        }

        System.out.println("現在の機種名： " + model.getModelName());
        System.out.print("新しい機種名（変更しない場合は空Enter） > ");
        String modelName = scanner.nextLine().trim();
        if (!modelName.isEmpty()) {
            model.setModelName(modelName);
        }

        System.out.println("現在の付属情報： " + model.getModelInfo());
        System.out.print("新しい付属情報（変更しない場合は空Enter） > ");
        String modelInfo = scanner.nextLine().trim();
        if (!modelInfo.isEmpty()) {
            model.setModelInfo(modelInfo);
        }

        modelDao.update(model);
        System.out.println("機種 [" + modelId + "] を更新しました。");
    }

    private void deleteModel() throws SQLException {
        System.out.print("削除対象の機種ID > ");
        String modelId = scanner.nextLine().trim();

        if (modelDao.findById(modelId) == null) {
            System.out.println("エラー： 機種ID [" + modelId + "] は存在しません。");
            return;
        }

        System.out.print("本当に削除しますか？（在庫・受注データが紐づいている場合は削除できません） [y/N] > ");
        String confirm = scanner.nextLine().trim();
        if (!"y".equalsIgnoreCase(confirm)) {
            System.out.println("削除をキャンセルしました。");
            return;
        }

        try {
            modelDao.delete(modelId);
            System.out.println("機種 [" + modelId + "] を削除しました。");
        } catch (SQLException e) {
            System.out.println("削除に失敗しました。在庫または受注データで参照されている可能性があります。");
            throw e;
        }
    }

    // =========================================================
    // 取引先マスタ
    // =========================================================
    private void manageClient() {
        while (true) {
            System.out.println();
            System.out.println("--- 取引先マスタ管理 ---");
            System.out.println(" 1. 一覧表示");
            System.out.println(" 2. 追加");
            System.out.println(" 3. 変更");
            System.out.println(" 4. 削除");
            System.out.println(" 0. 上のメニューへ戻る");
            System.out.print("番号を入力してください > ");

            String input = scanner.nextLine().trim();
            try {
                switch (input) {
                    case "1":
                        printClientList();
                        break;
                    case "2":
                        addClient();
                        break;
                    case "3":
                        updateClient();
                        break;
                    case "4":
                        deleteClient();
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("入力値が不正です。数字（0〜4）を入力してください。");
                }
            } catch (SQLException e) {
                System.out.println("エラーが発生しました： " + e.getMessage());
            }
        }
    }

    private void printClientList() throws SQLException {
        List<Client> list = clientDao.findAll();
        System.out.println();
        System.out.printf("%-10s %-25s %s%n", "取引先ID", "取引先名", "連絡先");
        for (Client client : list) {
            System.out.printf("%-10s %-25s %s%n", client.getClientId(), client.getClientName(), client.getClientContact());
        }
        System.out.println("件数： " + list.size());
    }

    private void addClient() throws SQLException {
        System.out.print("取引先ID > ");
        String clientId = scanner.nextLine().trim();

        if (clientDao.findById(clientId) != null) {
            System.out.println("エラー： 取引先ID [" + clientId + "] は既に存在します。");
            return;
        }

        System.out.print("取引先名 > ");
        String clientName = scanner.nextLine().trim();
        System.out.print("連絡先 > ");
        String clientContact = scanner.nextLine().trim();

        clientDao.insert(new Client(clientId, clientName, clientContact));
        System.out.println("取引先 [" + clientId + "] を追加しました。");
    }

    private void updateClient() throws SQLException {
        System.out.print("変更対象の取引先ID > ");
        String clientId = scanner.nextLine().trim();

        Client client = clientDao.findById(clientId);
        if (client == null) {
            System.out.println("エラー： 取引先ID [" + clientId + "] は存在しません。");
            return;
        }

        System.out.println("現在の取引先名： " + client.getClientName());
        System.out.print("新しい取引先名（変更しない場合は空Enter） > ");
        String clientName = scanner.nextLine().trim();
        if (!clientName.isEmpty()) {
            client.setClientName(clientName);
        }

        System.out.println("現在の連絡先： " + client.getClientContact());
        System.out.print("新しい連絡先（変更しない場合は空Enter） > ");
        String clientContact = scanner.nextLine().trim();
        if (!clientContact.isEmpty()) {
            client.setClientContact(clientContact);
        }

        clientDao.update(client);
        System.out.println("取引先 [" + clientId + "] を更新しました。");
    }

    private void deleteClient() throws SQLException {
        System.out.print("削除対象の取引先ID > ");
        String clientId = scanner.nextLine().trim();

        if (clientDao.findById(clientId) == null) {
            System.out.println("エラー： 取引先ID [" + clientId + "] は存在しません。");
            return;
        }

        System.out.print("本当に削除しますか？（受注データが紐づいている場合は削除できません） [y/N] > ");
        String confirm = scanner.nextLine().trim();
        if (!"y".equalsIgnoreCase(confirm)) {
            System.out.println("削除をキャンセルしました。");
            return;
        }

        try {
            clientDao.delete(clientId);
            System.out.println("取引先 [" + clientId + "] を削除しました。");
        } catch (SQLException e) {
            System.out.println("削除に失敗しました。受注データで参照されている可能性があります。");
            throw e;
        }
    }

    // =========================================================
    // 従業員マスタ
    // =========================================================
    private void manageEmployee() {
        while (true) {
            System.out.println();
            System.out.println("--- 従業員マスタ管理 ---");
            System.out.println(" 1. 一覧表示");
            System.out.println(" 2. 追加");
            System.out.println(" 3. 変更");
            System.out.println(" 4. 削除");
            System.out.println(" 0. 上のメニューへ戻る");
            System.out.print("番号を入力してください > ");

            String input = scanner.nextLine().trim();
            try {
                switch (input) {
                    case "1":
                        printEmployeeList();
                        break;
                    case "2":
                        addEmployee();
                        break;
                    case "3":
                        updateEmployee();
                        break;
                    case "4":
                        deleteEmployee();
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("入力値が不正です。数字（0〜4）を入力してください。");
                }
            } catch (SQLException e) {
                System.out.println("エラーが発生しました： " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("入社年度は数値で入力してください。");
            }
        }
    }

    private void printEmployeeList() throws SQLException {
        List<Employee> list = employeeDao.findAll();
        System.out.println();
        System.out.printf("%-10s %-15s %-10s %s%n", "従業員ID", "氏名", "入社年度", "部署");
        for (Employee employee : list) {
            System.out.printf("%-10s %-15s %-10s %s%n",
                    employee.getEmployeeId(), employee.getEmployeeName(), employee.getHireYear(), employee.getDepartment());
        }
        System.out.println("件数： " + list.size());
    }

    private void addEmployee() throws SQLException {
        System.out.print("従業員ID > ");
        String employeeId = scanner.nextLine().trim();

        if (employeeDao.findById(employeeId) != null) {
            System.out.println("エラー： 従業員ID [" + employeeId + "] は既に存在します。");
            return;
        }

        System.out.print("氏名 > ");
        String employeeName = scanner.nextLine().trim();
        System.out.print("入社年度（例：2024） > ");
        int hireYear = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("部署 > ");
        String department = scanner.nextLine().trim();

        employeeDao.insert(new Employee(employeeId, employeeName, hireYear, department));
        System.out.println("従業員 [" + employeeId + "] を追加しました。");
    }

    private void updateEmployee() throws SQLException {
        System.out.print("変更対象の従業員ID > ");
        String employeeId = scanner.nextLine().trim();

        Employee employee = employeeDao.findById(employeeId);
        if (employee == null) {
            System.out.println("エラー： 従業員ID [" + employeeId + "] は存在しません。");
            return;
        }

        System.out.println("現在の氏名： " + employee.getEmployeeName());
        System.out.print("新しい氏名（変更しない場合は空Enter） > ");
        String employeeName = scanner.nextLine().trim();
        if (!employeeName.isEmpty()) {
            employee.setEmployeeName(employeeName);
        }

        System.out.println("現在の入社年度： " + employee.getHireYear());
        System.out.print("新しい入社年度（変更しない場合は空Enter） > ");
        String hireYearStr = scanner.nextLine().trim();
        if (!hireYearStr.isEmpty()) {
            employee.setHireYear(Integer.parseInt(hireYearStr));
        }

        System.out.println("現在の部署： " + employee.getDepartment());
        System.out.print("新しい部署（変更しない場合は空Enter） > ");
        String department = scanner.nextLine().trim();
        if (!department.isEmpty()) {
            employee.setDepartment(department);
        }

        employeeDao.update(employee);
        System.out.println("従業員 [" + employeeId + "] を更新しました。");
    }

    private void deleteEmployee() throws SQLException {
        System.out.print("削除対象の従業員ID > ");
        String employeeId = scanner.nextLine().trim();

        if (employeeDao.findById(employeeId) == null) {
            System.out.println("エラー： 従業員ID [" + employeeId + "] は存在しません。");
            return;
        }

        System.out.print("本当に削除しますか？（受注データで担当者として紐づいている場合は削除できません） [y/N] > ");
        String confirm = scanner.nextLine().trim();
        if (!"y".equalsIgnoreCase(confirm)) {
            System.out.println("削除をキャンセルしました。");
            return;
        }

        try {
            employeeDao.delete(employeeId);
            System.out.println("従業員 [" + employeeId + "] を削除しました。");
        } catch (SQLException e) {
            System.out.println("削除に失敗しました。受注データで参照されている可能性があります。");
            throw e;
        }
    }
}
