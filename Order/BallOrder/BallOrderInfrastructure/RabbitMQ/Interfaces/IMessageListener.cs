namespace BallOrderInfrastructure.RabbitMQ.Interfaces
{
    public interface IMessageListener
    {
        void Start(IMessageManager callback);
        void Stop();
    }
}