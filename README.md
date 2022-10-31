# PdfPipe - PDF Merge & Rearrange Tool
PdfPipe is command line tool. You must have Java installed and should know the basics of CMD/SH.

Every command has to start with `java -jar PdfPipe.jar <Arguments>` (replace `PdfPipe.jar` with the path to the jar file of PdfPipe)

## Simple Merge
Merge **input1.pdf** and **input2.pdf** into **output.pdf**\
(Specify input files with `-i`, the output file with `-o`)\
`-i input1.pdf input2.pdf -o output.pdf`

Merge all pages of **input1.pdf** and page 5 of **input2.pdf** into **output.pdf**\
(specify the output structure with `-s`. "1" and "2" are the indexes of the given documents, "5" is the page number)\
`-i input1.pdf input2.pdf -s 1 2:5 -o output.pdf`

## Rearrange pages of PDFs
Switch first page with second page and keep the rest of **Test.pdf** (assuming it has 3 pages)\
(Use `-f` to overwrite the existing file "Test.pdf")\
`-i Test.pdf -s 1:2,1,3 -o Test.pdf -f`

Only keep first 5 pages of **Invoice.pdf**\
`-i Invoice.pdf -s 1:1-5 -o Invoice.pdf -f`

Reverse all pages of **Test.pdf** (assuming it has 7 pages)\
`-i Test.pdf -s 1:7-1 -o Test.pdf -f`

Duplicate page 1 of **Test.pdf** (assuming it has 5 pages)\
`-i Test.pdf -s 1:1,1,2-5 -o Test.pdf -f`

Only keep page 4 and 5 of **Test.pdf** and set rotation of first page in the output (so page 4) to 180 degrees\
(use `-r` to set the rotation of output pages)\
`-i Test.pdf -s 1:4,5 -o Test.pdf -r 1:180 -f`

## And so on...
Check the arguments and a few more examples below to see what else you can do.

# Arguments/Usage
| Short | Full                   | Description                                             |
|-------|------------------------|---------------------------------------------------------|
| -i    | --input \<files\>      | Input files, e.g: `-i file1.pdf file2.pdf`              |
| -s    | --structure \<args\>   | Document/page structure. See examples below.            |
| -o    | --output \<path\>      | Output file, e.g.: `-o output.pdf`                      |
| -f    | --force                | Force creating file, even if output file already exists |
| -r    | --rotation \<degrees\> | Rotation of the output pages. See examples below.       |
| -t    | --title \<title\>      | Metadata title of the PDF file (not file name)          |
| -a    | --author \<author\>    | Metadata author of the PDF file                         |
|       | \<without args\>       | Display usage help                                      |

## Structure Examples
`-s 1 2`\
All pages of first file, all pages of second file. This is the default behaviour.

`-s 2:1,3 1:3 3:1-4`\
Only first and third page of second file, only third page of first file, pages 1-4 of third file.

`-s 1:3,2,4 3 3 2:1,1,1`\
Third, second and fourth page of first file, two times all pages of third file, three times the first page of the second file.

`-s 2:5 1:5-1,7`\
Fifth page of second file, pages 5-1 (reverse order) and page 7 of first file.

## Rotation Examples
The given page numbers are the page numbers of the output file.

`-r 1:90`\
Set rotation of first page to 90 degrees, leave all other pages as they are.

`-r 1-4,7:180 8-12:270`\
Set rotation of pages 1-4 and page 7 to 180 degrees, set rotation of pages 8-12 to 270 degrees, leave all other pages as they are.

# pdfpipe Command on Windows
A short explanation on how to create a pdfpipe command you can use everywhere on Windows.
1. Create the folder you want to add to the Path variable, for example `C:\Users\USERNAME\bin`
2. Copy the PdfPipe jar file into this folder
3. Create a new file called `pdfpipe.cmd` in this folder (in this case the command name will be `pdfpipe`)
4. Put the following content in this file:
   ```cmd
   @echo off
   java -jar "C:\Users\USERNAME\bin\PdfPipe-1.1.jar" %*
   ```
   Replace `C:\Users\USERNAME\bin\PdfPipe-1.1.jar` with the absolute path of the PdfPipe jar file.\
   (The `%*` passes all arguments of the cmd script to the jar file.)
5. Open your Path settings (usually by searching "path" on Windows)
6. Select the variable "Path" either in the user or system variables, and click "Edit"
7. Click "New" on the top right and paste the path of the folder above, for example `C:\Users\USERNAME\bin`
8. Close everything with OK.
9. You should be able to use the `pdfpipe` command in a CMD or PowerShell window now, regardless of in which folder you are (you have to reopen already opened CMD/PowerShell windows to reload the path variables).

# License
This code is under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).
