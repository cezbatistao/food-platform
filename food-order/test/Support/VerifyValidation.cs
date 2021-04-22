using System.Collections.Generic;

namespace test.Support
{
    public class VerifyValidation
    {
        public int TotalErrors { get; set; }
        public List<string> MessagesErrors { get; set; }

        public VerifyValidation(int totalErrors, List<string> messagesErrors)
        {
            this.TotalErrors = totalErrors;
            this.MessagesErrors = messagesErrors;
        }
    }
}