using Moq;
using PaymentAPI.Controllers;
using PaymentDomain.Services;
using System;
using Xunit;

namespace PaymentTest
{
    public class UnitTest1
    {
        [Fact]
        public void Test1()
        {
            //Arrange
            var repo = new Mock<IPaymentRepository>();
            var controller = new PaymentsController();

            //Act
            controller.Post("Hoi");
            
            //Assert
        }
    }
}
