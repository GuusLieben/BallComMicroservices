namespace Infrastructure.RabbitMQ.Interfaces
{
    public interface IMessageListener
    {
        void Start(IMessageHandler callback);
        void Stop();
    }
}
