   M S B A M L   `   `   `  ����   
URC_Server%    URC_Server.Controls.Inicio_xaml   \ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     @d2http://schemas.microsoft.com/expression/blend/2008     $	��400��$	��550��.���   � ColumnDefinitions  �� $
��AUTO ��� .+��� -   $��ListaConexiones��$��1��$	��210�����	#00000000$��0q����� *Inicio+ �� SelectedItem �� *SelectedInicio+ �� SelectedIndex �� *SelectedInicioIndex+���� .R��  � RowDefinitions �� $
��Auto ��� .+��� $��0��$	��198�����#399CFC���#00468A$��15(�$��SemiBold"� �� Padding$ 4q����� *	GroupName+y��� *ShowGroupName+�� $��1��.+��� $��15(����#5F5F60$ 
10 10 0 10q�$
��Wrap=����� *Name+����  �� 
TargetType" � �� Triggers P�  P� Property �@J�IsMouseOver P� Value . . ��� 	 �� Property	 �@�Cursor
 �� Value$

 Hand��� $��1�� �� $
��Auto ��� �� $
��Auto �.+��� -   $
��Mask�����#87c3fd���#87c3fd$��.5q� �� CornerRadius$ 13���� .+��� -   $��princ_image��$��0��$	��280������ *InicioImage+ J� OpacityMask !�  !� Visual ��  �� ElementName$
 Mask���� $��1��$
��Wrap=�$��Center=�$��Center=� �� TextAlignment$ Center=�$��15(����� *
InicioText+� $��2��$��0 0 0 10q�  �� �� .+��� ���	#00000000$��0q� � ToolTip$ Pasar imagen������ *	LastImage+.���� $��0��$ ��/Resources/Inicio/next.png��$��60������ " �� P�  �@J�IsMouseOver . . ��� 	 �@�Cursor$

 Hand���� $��1�����	#00000000$��0q�$ Pasar imagen������ *	NextImage+.���� $ ��/Resources/Inicio/next.png��$��60�� � LayoutTransform ��  �� Angle$	 180Q����� " �� P�  �@J�IsMouseOver . . ��� 	 �@�Cursor$

 Hand��