public class ShowSyntaxArgs {

    public static void showTemplateArgs(String process) {
        switch (process){
            case "Student":
                System.out.println("EmpaquetarExamen");
                System.out.println("\tArgs Syntax: ExamFile PackageName Student.private Teacher.public ");
                System.out.println();
                break;
            case "SealingAuthority":
                System.out.println("SellarExamen");
                System.out.println("\tArgs Syntax: PackageName Student.public SealingAuthority.private ");
                System.out.println();
                break;
            case "Teacher":
                System.out.println("DesempaquetarExamen");
                System.out.println("\tArgs Syntax: PackageName ExamFile Teacher.private Student.public SealingAuthority.public ");
                System.out.println();
                break;
            default:
                System.out.println("Error");
                break;
        }

    }
}
