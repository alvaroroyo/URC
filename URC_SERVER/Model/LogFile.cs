using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace URC_Server.Model
{
    class LogFile
    {
        public static void Log(string titulo,string sms)
        {
            try
            {
                using (StreamWriter writer = File.AppendText("Log.txt"))
                {
                    writer.Write("\r\n---- " + DateTime.Now + " ----");
                    writer.WriteLine("\r\n\t" + titulo + ":");
                    writer.WriteLine("\r\n\t" + sms);
                    writer.WriteLine("----------------------------");
                }
            }
            catch (Exception e) { }
        }
    }
}
