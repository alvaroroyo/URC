   M S B A M L   `   `   `  ����  
URC_Server%    URC_Server.Controls.Inicio_xaml   5      \ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    5      R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5      Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     5      @d2http://schemas.microsoft.com/expression/blend/2008     5      $	��400��5      $	��550��6   .��5   
   �   � ColumnDefinitions  5      �� 5	      $
��AUTO �6#   �� 5
      .+�5   
   �� -   5        $��ListaConexiones��5      $��1��6   $	��210��66   ���	#00000000$��0q�6Y   ��6m   �� *Inicio+ �� SelectedItem 6�   �� *SelectedInicio+ �� SelectedIndex 6�   �� *SelectedInicioIndex+��5      �� 5      .R�5      �  � RowDefinitions 5      �� 5   "   $
��Auto �60   �� 5   "   .+�5      �� $��0��6(   $	��198��65   ���#399CFC���#00468A$��15(�6�   $��SemiBold"�6�    �� Padding$ 4q�6�   ��6V   �� *	GroupName+y�6q   �� *ShowGroupName+6�   �� 5      $��1��6)   .+�5   "   �� $��15(�6,   ���#5F5F60$ 
10 10 0 10q�6e   $
��Wrap=�6z   ��6O   �� *Name+6z   5      ��5      �� 5       �� 
TargetType" �6$    �� Triggers 5   "   P� 5   &    P� Property �@J�IsMouseOver P� Value . . �5   *   �� 	 �� Property	 �@�Cursor
 �� Value$

 Hand��6C   5    '   5"      5$      5%      5'      � 5)      $��1��6    5*      �� 5+      $
��Auto �6$   �� 5,      �� 5-      $
��Auto �6$   .+�50      �� -   5        $
��Mask��50      ���#87c3fd���#87c3fd$��.5q�6L    �� CornerRadius$ 13��6a   �� 51      .+�52      �� -   5        $��princ_image��52   )   $��0��6   $	��280��6[   ��6<   �� *InicioImage+6[    J� OpacityMask 53      !� 54       !� Visual 6&   ��  �� ElementName$
 Mask��56      �� 59      $��1��6   $
��Wrap=�6E   $��Center=�6Y   $��Center=�6v    �� TextAlignment$ Center=�6�   $��15(�6�   ��6)   �� *
InicioText+6�   � 5;      $��2��6   $��0 0 0 10q�6      5<      �� 5=      �� 5>      .+�5A      �� ���	#00000000$��0q�6N    � ToolTip$ Pasar imagen��6b   ��6   �� *	LastImage+.��5B      �� $��0��6   $ ��/Resources/Inicio/next.png��6,   $��60��6P   ��5D      �� 5E      " ��6     5F      P� 5G   "    �@J�IsMouseOver . . �5H   &   �� 	 �@�Cursor$

 Hand��6?   5I   #   5K      5N      �� 5P      $��1��6   ���	#00000000$��0q�6^   $ Pasar imagen��6r   ��6)   �� *	NextImage+.��5Q      �� $ ��/Resources/Inicio/next.png��6   $��60��6@    � LayoutTransform 5R      �� 5S       �� Angle$	 180Q�6.   5U      ��5W      �� 5X      " ��6     5Y      P� 5Z   "    �@J�IsMouseOver . . �5[   &   �� 	 �@�Cursor$

 Hand��6?   5\   #   5^      5a      5b      5d      5f      5g      