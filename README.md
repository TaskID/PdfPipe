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

## And so on...
Check the arguments and a few more examples below to see what else you can do.

# Arguments/Usage
| Short | Full                 | Description                                             |
|-------|----------------------|---------------------------------------------------------|
| -i    | --input \<files\>    | Input files, e.g: `-i file1.pdf file2.pdf`              |
| -s    | --structure \<args\> | Document/page structure. See examples below.            |
| -o    | --output \<path\>    | Output file, e.g.: `-o output.pdf`                      |
| -f    | --force              | Force creating file, even if output file already exists |
| -t    | --title \<title\>    | Metadata title of the PDF file (not file name)          |
| -a    | --author \<author\>  | Metadata author of the PDF file                         |
|       | \<without args\>     | Display usage help                                      |

## Structure Examples
`-s 1 2`\
All pages of first file, all pages of second file. This is the default behaviour.

`-s 2:1,3 1:3 3:1-4`\
Only first and third page of second file, only third page of first file, pages 1-4 of third file.

`-s 1:3,2,4 2 2 3:1,1`\
Third, second and fourth page of first file, two times all pages of second file, two times the first page of the third file.

`-s 2:5 1:5-1,7`\
Fifth page of second file, pages 5-1 (reverse order) and page 7 of first file.

# License
This code is under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).
