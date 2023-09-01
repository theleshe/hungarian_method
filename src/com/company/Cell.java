package com.company;

public class Cell {
    public int value;
    public boolean isChecked = false;      //переменная, используемая в проверке на оптимальность
    public int state = 0;       //  состояние ячейки, где 0 - не вычеркнута     1 - находится на одной прямой       2 - находится на пересечении прямых
    public boolean isSelected = false;

    public Cell (int value)
    {
        this.value = value;
    }
}
