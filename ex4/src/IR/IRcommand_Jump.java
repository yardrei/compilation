package IR;

public abstract class IRcommand_Jump extends IRcommand
{
    public String label_name;

    public IRcommand_Jump(String label)
    {
        this.label_name = label;
    }

}