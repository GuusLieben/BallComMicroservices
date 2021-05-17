namespace BallOrder.Models
{
    public enum OrderState
    {
        InitialOrder = 0,
        WaitingForStock = 1,
        StartPicking = 2,
        OrderFinished = 3
    }
}