using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Forms;

namespace URC_Server.Model
{
    /// <summary>
    /// Created by Alvaro Royo
    /// Manage mouse clicks, multiple keyboard events and mouse moves.
    /// Check our methods.
    /// </summary>
    public class KeyManager
    {
        private const uint KEY_DOWN_EVENT = 0x0001;
        private const uint KEY_UP_EVENT = 0x0002;

        private const int MOUSEEVENTF_LEFTDOWN = 0x02;
        private const int MOUSEEVENTF_LEFTUP = 0x04;
        private const int MOUSEEVENTF_RIGHTDOWN = 0x08;
        private const int MOUSEEVENTF_RIGHTUP = 0x10;
        /// <summary>
        /// If dwFlags contains MOUSEEVENTF_WHEEL, then dwData specifies the amount of wheel movement.
        /// A positive value indicates that the wheel was rotated forward.
        /// a negative value indicates that the wheel was rotated backward.
        /// </summary>
        private const int MOUSEEVENTF_WHEEL = 0x0800;

        Dictionary<Keys, bool> pulsaciones;

        public KeyManager()
        {
            pulsaciones = new Dictionary<Keys, bool>();
        }

        #region DLL_Imports

        [DllImport("user32.dll")]
        public static extern void keybd_event(byte bVk, byte bScan, uint dwFlags, uint dwExtraInfo);
        [DllImport("user32.dll", SetLastError = true)]
        [return: MarshalAs(UnmanagedType.Bool)]
        public static extern bool SetCursorPos(int X, int Y);
        [DllImport("user32.dll")]
        static extern bool GetCursorPos(out Position position);
        [DllImport("user32.dll", CharSet = CharSet.Auto, CallingConvention = CallingConvention.StdCall)]
        public static extern void mouse_event(int dwFlags, int dx, int dy, int dwData, int dwExtraInfo);

        #endregion

        [StructLayout(LayoutKind.Sequential)]
        public struct Position
        {
            public int X;
            public int Y;
        }

        #region KeyBoardEvents

        /// <summary>
        /// Press and hold a key. Use keyRelease(Keys key) for release
        /// Only can press and hold a key one by one.
        /// </summary>
        /// <param name="key">The key you want to press</param>
        /// <param name="speed">The press speed. Normal speed: 100 ~ 120</param>
        public void keyPress(Keys key, int speed)
        {
            try
            {
                pulsaciones.Add(key, true);
                (new Thread(() => {
                    while (pulsaciones.ContainsKey(key)) 
                    {
                        keybd_event((byte)key, 0x45, KEY_DOWN_EVENT, 0);
                        Thread.Sleep(speed); 
                    } 
                })).Start();
            }
            catch (ArgumentException){}
        }
        /// <summary>
        /// Press and hold a key
        /// </summary>
        /// <param name="key">The key you want to press</param>
        public void keyPress(Keys key)
        {
            try
            {
                pulsaciones.Add(key, true);
                (new Thread(() => {
                    while (pulsaciones.ContainsKey(key)) 
                    {
                        keybd_event((byte)key, 0x45, KEY_DOWN_EVENT, 0);
                        Thread.Sleep(120); 
                    } 
                })).Start();
            }
            catch (ArgumentException) { }
        }
        
        /// <summary>
        /// Release a hold key
        /// </summary>
        /// <param name="key"></param>
        public void keyRelease(Keys key)
        {
            keybd_event((byte)key, 0x45, KEY_UP_EVENT, 0);
            pulsaciones.Remove(key);
        }

        /// <summary>
        /// Simulate a simple key pulsation
        /// </summary>
        /// <param name="key">The key you want to press down</param>
        public void keyDown(Keys key)
        {
            keybd_event((byte)key, 0x45, KEY_DOWN_EVENT, 0);
            keybd_event((byte)key, 0x45, KEY_UP_EVENT, 0); 
        }

        ///// <summary>
        ///// Simulate multiple key down
        ///// </summary>
        ///// <param name="key">Array of keys</param>
        //public void keyDownMultiple(Keys[] key)
        //{
        //    for (int i = 0; i < key.Length; i++)
        //    {
        //        keybd_event((byte)key[i], 0, 0, 0);
        //    }
        //    for (int i = 0; i < key.Length; i++)
        //    {
        //        keybd_event((byte)key[i], 0, KEY_UP_EVENT, 0);
        //    }
        //}

        #endregion

        #region MouseEvents

        public int getMouseX()
        {
            Position pos;
            GetCursorPos(out pos);
            return pos.X;
        }

        public int getMouseY()
        {
            Position pos;
            GetCursorPos(out pos);
            return pos.Y;
        }

        public void mouseMove(int x, int y)
        {
            SetCursorPos(x,y);
        }

        public void mouseLeftClick()
        {
            int x = getMouseX();
            int y = getMouseY();
            mouse_event(MOUSEEVENTF_LEFTDOWN | MOUSEEVENTF_LEFTUP, x, y, 0, 0);
        }

        public void mouseLeftClick(int x, int y)
        {
            mouse_event(MOUSEEVENTF_LEFTDOWN | MOUSEEVENTF_LEFTUP, x, y, 0, 0);
        }

        public void mouseRightClick()
        {
            int x = getMouseX();
            int y = getMouseY();
            mouse_event(MOUSEEVENTF_RIGHTDOWN | MOUSEEVENTF_RIGHTUP, x, y, 0, 0);
        }

        public void mouseRightClick(int x, int y)
        {
            mouse_event(MOUSEEVENTF_RIGHTDOWN | MOUSEEVENTF_RIGHTUP, x, y, 0, 0);
        }

        public void mouseWheel(int mouseWheel)
        {
            mouse_event(MOUSEEVENTF_WHEEL, getMouseX(), getMouseY(), mouseWheel, 0);
        }

        public void mouseLeftClickDown()
        {
            mouse_event(MOUSEEVENTF_LEFTDOWN, getMouseX(), getMouseY(), 0, 0);
        }

        public void mouseLeftClickUp()
        {
            mouse_event(MOUSEEVENTF_LEFTUP, getMouseX(), getMouseY(), 0, 0);
        }

        public void mouseRightClickDown()
        {
            mouse_event(MOUSEEVENTF_RIGHTDOWN, getMouseX(), getMouseY(), 0, 0);
        }

        public void mouseRightClickUp()
        {
            mouse_event(MOUSEEVENTF_RIGHTUP, getMouseX(), getMouseY(), 0, 0);
        }
        #endregion
    }
}
